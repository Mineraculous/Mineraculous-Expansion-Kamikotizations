# @thomasglasser's Code Style & Expectations Guide

Based on a comprehensive review of over 1,000 pull request review comments left by `@thomasglasser` in the `Mineraculous` repository, here is a detailed synthesis of his code style preferences, architectural expectations, and pet peeves.

## 1. Naming & Terminology

*   **Be Descriptive & Clear**: Avoid vague names like `null`, `trigger`, `commander`, or generic letters (`d`, `a`, `g`). Use descriptive action-oriented names for methods (e.g., `enqueueTransformation` instead of `transform`, `normalizeDegreesBetween180` instead of `normalizeDegrees`).
*   **No Abbreviations**: Do not abbreviate variable names (e.g., avoid `MLB` for Miraculous Ladybug, or `ent` for `entity`). Use the full word.
*   **Java Conventions**: Follow standard Java naming conventions strictly.
*   **Constant Naming**: `public static final` (PSF) fields must use `CAPITAL_SNAKE_CASE`.
*   **Pluralization**: Be accurate with plurals (e.g., `miraculouses`, `options` instead of `option` for lists).
*   **Event Methods**: Do not include `Event` in event handler method names (e.g., use `onLivingFall` instead of `onLivingFallEvent`).
*   **Avoid "Tool" for Non-Tools**: Reserve the word "tool" for actual tools (pickaxes, axes, etc.). Do not use it for misc items or magical items.

## 2. Formatting, Layout & Organization

*   **Alphabetical & Logical Ordering**:
    *   Lists, registries, and data generation entries must be in alphabetical order.
    *   For visual order (like Creative Tabs or recipes), group related items logically and consistently (e.g., "glue, tape, rope").
*   **Class Structure**:
    *   **Public before Private**: Public methods and classes go above private ones.
    *   **Inner Classes & Enums at the Bottom**: Records, inner classes, and enums must be placed below all methods at the very bottom of the class.
    *   **Variable Placement**: Declare variables right above where they are used, rather than at the top of a method.
*   **Clean Control Flow**: Use `else if` chains rather than separate isolated `if` statements. Combine boolean conditions using `&&` and `||` wherever possible to avoid nested `if` statements.
*   **Spacing**: Do not use unnecessary blank lines or spaces. Keep related logic tightly grouped.
*   **Javadocs**:
    *   Use `///` exclusively for single-line javadocs. Use `//` for normal comments (like `// TODO`).
    *   Class javadocs are expected, but implementation details (`impl`) do not need them unless necessary.
    *   Put separate sentences on separate lines within javadocs.
    *   Place javadocs on the methods/classes themselves, not on specific registry entries.
*   **Static Imports**: Do NOT use static imports.

## 3. Performance & Efficiency

*   **Avoid Object Allocation in Ticks**: Never create new objects (like `new ResourceLocation(...)`, `new Vec3(...)`) inside `tick()` or other high-frequency methods. Cache them as constants or fields.
*   **Fast Collections**: Avoid standard Java collections (`ArrayList`, `HashMap`) in favor of Guava or FastUtil equivalents (e.g., `ReferenceOpenHashSet`, `ObjectArrayList`) to improve performance and memory usage.
*   **No Streams**: Avoid using Java Streams API (`.stream().filter()...`), especially in performance-sensitive areas.
*   **Immutability for API**: Use `ImmutableList`, `ImmutableSet`, etc., when returning collections in APIs.
*   **BlockPos.Mutable**: Use `BlockPos.Mutable` when iterating or performing many positional checks instead of creating new `BlockPos` objects.
*   **Optimized Checks**: Move expensive checks out of loops (e.g., checking if a GUI is open should happen before a loop, not inside). For expensive repeating logic, throttle it (e.g., `if (entity.tickCount % 10 == 0)`).

## 4. Minecraft & NeoForge Modding Practices

*   **Client vs. Server Separation**:
    *   Never use client-only classes (`Minecraft`, `KeyMapping`) in common code (this crashes dedicated servers). Use `ClientUtils.getLocalPlayer` for side-safety.
    *   Perform physics, delta movement, and heavy logic calculations on the server, not the client.
*   **Leverage Built-In Utils**:
    *   Use `TommyLib` features (e.g., `AnimationUtils`, `TommyLibExtraStreamCodecs`) instead of reinventing the wheel.
    *   Do not manually register things that are handled automatically (like Curios renderers).
*   **Data Attachments**:
    *   When fetching attachments, use `player.getData(...)` optimally. Don't fetch it twice.
    *   Do not call `remove()` every tick if the data isn't present. Check `.isPresent()` (not `.hasData()`).
*   **Tags Over Hardcoding**: Do not hardcode specific items or blocks in logic. Create and use tags (e.g., `hibiscus_bushes`, `removed_by_rinsing`).
*   **Constants**: Use vanilla constants whenever possible (e.g., `Block` constants for `setBlock`, `OminousBottleItem.EFFECT_DURATION`).
*   **Generics**: Avoid raw use of generic classes. Use `<?>` if the bounds do not matter.

## 5. Code Quality & Cleanliness

*   **Inlining**: Inline variables and methods that are only used once or simply wrap a single call. If a method takes 10 lines but can be written in 2 using built-ins, condense it.
*   **DRY (Don't Repeat Yourself)**:
    *   Extract duplicated logic into parent classes or utility methods.
    *   If you are overriding a method just to call `super.method()`, remove the override entirely.
*   **Remove Dead Code**: Never commit commented-out code, unused variables, or unused generic parameters.
*   **No `var`**: Do not use the `var` keyword. Explicitly define variable types.
*   **Placeholder Assets**: Do not commit placeholder textures or sounds without clear indications or intent to replace them. Use final assets where possible.
*   **Return Types**: Design APIs so methods return booleans (e.g., to indicate success/failure or if an event should be canceled) rather than relying on side effects.

## 6. Git & Review Etiquette

*   **Pay Attention to IDE Warnings**: "This is comparing `ResourceKey`s to a `ResourceLocation`, pay attention to IDE warnings."
*   **Review Your Own PRs**: "clearly not looking over changes before pushing." He expects contributors to self-review and clean up code (formatting, removing debug prints) before asking for a review.
*   **Don't Fix Things Without Actually Fixing Them**: "Still applies, why did you resolve a bunch of things without fixing them?" Do not resolve review comment threads unless the issue has been completely addressed in the code.
