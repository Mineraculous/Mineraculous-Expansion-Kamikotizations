package dev.thomasglasser.mineraculouskamikotizations.data.recipes;

import dev.thomasglasser.mineraculous.api.data.recipes.MineraculousRecipeProvider;
import dev.thomasglasser.mineraculous.api.tags.MineraculousTagUtils;
import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsItemTags;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

public class MineraculousKamikotizationsRecipeProvider extends MineraculousRecipeProvider {
    public MineraculousKamikotizationsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider registries) {
        for (DyeColor color : DyeColor.values()) {
            TagKey<Item> dyeTag = MineraculousTagUtils.forDyeColor(color);

            parasolFromCarpet(recipeOutput, MineraculousKamikotizationsItems.PARASOLS.get(color).get(), BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getName() + "_carpet")));
            itemFromDye(recipeOutput, MineraculousKamikotizationsItems.PARASOLS.get(color).get(), MineraculousKamikotizationsItemTags.PARASOLS, dyeTag);
        }
    }

    protected void parasolFromCarpet(RecipeOutput recipeOutput, ItemLike parasol, ItemLike carpet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, parasol)
                .pattern(" C ")
                .pattern("CSC")
                .pattern(" S ")
                .define('C', carpet)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy(getHasName(carpet), has(carpet))
                .unlockedBy(getHasName(MineraculousKamikotizationsItemTags.PARASOLS), has(MineraculousKamikotizationsItemTags.PARASOLS))
                .save(recipeOutput);
    }

    protected void itemFromDye(RecipeOutput recipeOutput, ItemLike result, TagKey<Item> itemTag, TagKey<Item> dyeTag) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, result)
                .requires(itemTag)
                .requires(dyeTag)
                .unlockedBy(getHasName(itemTag), has(itemTag))
                .save(recipeOutput, RecipeBuilder.getDefaultRecipeId(result).withPath(path -> path + "_from_dye"));
    }
}
