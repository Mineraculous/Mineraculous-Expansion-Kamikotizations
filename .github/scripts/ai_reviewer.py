import os
import json
import logging
from github import Github, GithubException, Auth
from google import genai
from google.genai import types

# Setup logging
logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

def main():
    # 1. Load environment variables
    github_token = os.environ.get("GITHUB_TOKEN")
    gemini_api_key = os.environ.get("GEMINI_API_KEY")
    github_repository = os.environ.get("GITHUB_REPOSITORY") # e.g., Mineraculous/Mineraculous
    pr_number_str = os.environ.get("PR_NUMBER")
    
    if not all([github_token, gemini_api_key, github_repository, pr_number_str]):
        logging.error("Missing required environment variables.")
        exit(1)
        
    pr_number = int(pr_number_str)
    
    # 2. Initialize APIs
    auth = Auth.Token(github_token)
    g = Github(auth=auth)
    repo = g.get_repo(github_repository)
    pr = repo.get_pull(pr_number)
            
    client = genai.Client(api_key=gemini_api_key)
    
    # 3. Get the PR diff and latest commit
    logging.info(f"Fetching PR #{pr_number} diff...")
    import requests
    headers = {"Authorization": f"token {github_token}", "Accept": "application/vnd.github.v3.diff"}
    resp = requests.get(pr.url, headers=headers)
    
    
    if resp.status_code != 200:
        error_msg = f"Failed to fetch PR diff (HTTP {resp.status_code})"
        logging.error(error_msg)
        exit(1)
        
    pr_diff = resp.text
    latest_commit = pr.get_commits().reversed[0]
    
    if not pr_diff.strip():
        logging.info("No changes in this PR.")
        return

    # 4. Read the Style Guide
    script_dir = os.path.dirname(os.path.abspath(__file__))
    style_guide_path = os.path.join(script_dir, "thomasglasser_code_style_guide.md")
    
    with open(style_guide_path, "r", encoding="utf-8") as f:
        style_guide = f.read()

    # 5. Construct Prompt
    logging.info("Analyzing with Gemini...")
    prompt = f"""
You are Thomas Glasser, an expert Minecraft/NeoForge mod developer and strict code reviewer.
Review the following Pull Request diff against your strict code style preferences and expectations.

### YOUR STYLE GUIDE:
{style_guide}

### PULL REQUEST DIFF:
```diff
{pr_diff}
```

### INSTRUCTIONS:
1. Identify any violations of your style guide or obvious bugs in the code.
2. Return your review comments as a strict JSON array of objects.
3. Each object must have three keys: 
   - `path`: The relative file path of the file being reviewed.
   - `line`: The exact line number in the NEW version of the file (right side of the diff) where the issue occurs.
   - `body`: Your review comment (in your characteristic concise, direct, and instructional tone).
4. If there are no issues, return an empty array `[]`.
5. DO NOT wrap the JSON in Markdown code blocks like ```json ... ```. Just return the raw JSON array.
"""

    try:
        response = client.models.generate_content(
            model='gemini-3.6-flash',
            contents=prompt,
            config=types.GenerateContentConfig(
                temperature=0.2,
                response_mime_type="application/json"
            )
        )
        
        raw_json = response.text.strip()
        
        if raw_json.startswith("```json"):
            raw_json = raw_json[7:]
        if raw_json.startswith("```"):
            raw_json = raw_json[3:]
        if raw_json.endswith("```"):
            raw_json = raw_json[:-3]
            
        comments_data = json.loads(raw_json.strip())
        
    except Exception as e:
        error_str = str(e).lower()
        if "429" in error_str or "quota" in error_str or "exhausted" in error_str:
            quota_msg = "😴 **TommyBot is resting:** I've run out of AI quota for today (limit reached)! Please try again tomorrow."
            logging.error(quota_msg)
            try:
                pr.as_issue().create_comment(quota_msg)
            except Exception as inner_e:
                logging.error(f"Failed to post quota message: {inner_e}")
        else:
            error_msg = f"Failed to generate or parse AI response.\n{e}"
            logging.error(error_msg)
            
        exit(1)

    # 6. Post Comments
    if not comments_data:
        logging.info("No style violations found. Thomas is pleased.")
        return
        
    logging.info(f"Found {len(comments_data)} potential issues to report.")
    
    general_comments = []
    
    for item in comments_data:
        path = item.get("path")
        line = item.get("line")
        body = item.get("body")
        
        if not path or not line or not body:
            continue
            
        try:
            pr.create_review_comment(
                body=body,
                commit=latest_commit,
                path=path,
                line=line,
                side="RIGHT"
            )
            logging.info(f"Posted inline comment on {path}:{line}")
        except GithubException as e:
            logging.warning(f"Could not post inline comment on {path}:{line}. Reason: {e.data.get('message')}")
            general_comments.append(f"- **{path}:{line}**: {body}")

    if general_comments:
        review_body = "I couldn't attach these comments to specific lines in the diff, but please address them:\n\n"
        review_body += "\n".join(general_comments)
        pr.create_issue_comment(review_body)
        logging.info("Posted general fallback comment.")

if __name__ == "__main__":
    main()
