package dev.thomasglasser.mineraculouskamikotizations.data.models;

import dev.thomasglasser.mineraculous.api.client.renderer.item.MineraculousItemProperties;
import dev.thomasglasser.mineraculous.api.world.item.MineraculousItemDisplayContexts;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import dev.thomasglasser.tommylib.api.data.models.ExtendedItemModelProvider;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MineraculousKamikotizationsItemModelProvider extends ExtendedItemModelProvider {
    public MineraculousKamikotizationsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MineraculousKamikotizations.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation parasolLoc = MineraculousKamikotizationsItems.PARASOLS.get(color).getId();
            ResourceLocation openLoc = parasolLoc.withSuffix("_open");
            withSeparateInventoryModel(parasolLoc, withExistingParent(parasolLoc.getPath(), modLoc("item/parasol_closed"))
                    .texture("texture", parasolLoc.withPrefix("item/"))
                    .texture("particle", parasolLoc.withPrefix("item/")), basicItem(parasolLoc.withSuffix("_inventory")))
                            .override()
                            .predicate(MineraculousItemProperties.ACTIVE, 1)
                            .model(withSeparateInventoryModel(openLoc, withExistingParent(openLoc.getPath(), modLoc("item/parasol_open"))
                                    .texture("texture", openLoc.withPrefix("item/"))
                                    .texture("particle", openLoc.withPrefix("item/")), basicItem(openLoc.withSuffix("_inventory"))))
                            .end();

            basicItem(MineraculousKamikotizationsItems.BUBBLE_WANDS.get(color).get());
        }

        // TODO: Convert to look model
        ItemModelBuilder inHandBubbleSword = withExistingParent("item/bubble_sword_in_hand", modItemLoc("bubble_sword_in_hand"));
        ItemModelBuilder inventoryBubbleSword = basicItem(MineraculousKamikotizationsItems.BUBBLE_SWORD.getId().withPath(path -> path + "_inventory")).guiLight(BlockModel.GuiLight.FRONT);
        ItemModelBuilder onBackBubbleSword = withExistingParent("item/bubble_sword_on_back", modItemLoc("bubble_sword_in_hand"))
                .texture("texture", modItemLoc("bubble_sword_on_back"))
                .transforms()
                .transform(MineraculousItemDisplayContexts.CURIOS_BODY.getValue())
                .translation(0, -2.5F, 4.5F)
                .end()
                .end();
        generatedModels.remove(inHandBubbleSword.getLocation());
        generatedModels.remove(inventoryBubbleSword.getLocation());
        generatedModels.remove(onBackBubbleSword.getLocation());
        withSeparateTransforms(MineraculousKamikotizationsItems.BUBBLE_SWORD)
                .base(inHandBubbleSword)
                .perspective(ItemDisplayContext.GUI, inventoryBubbleSword)
                .perspective(ItemDisplayContext.FIXED, inventoryBubbleSword)
                .perspective(ItemDisplayContext.GROUND, inventoryBubbleSword)
                .perspective(MineraculousItemDisplayContexts.CURIOS_BODY.getValue(), onBackBubbleSword);
    }
}
