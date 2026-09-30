package dev.thomasglasser.mineraculouskamikotizations.data.models;

import dev.thomasglasser.mineraculous.api.client.renderer.item.MineraculousItemProperties;
import dev.thomasglasser.mineraculous.api.data.models.MineraculousItemModelProvider;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MineraculousKamikotizationsItemModelProvider extends MineraculousItemModelProvider {
    public MineraculousKamikotizationsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MineraculousKamikotizations.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerLookTransforms();

        // Look Powered
        withEntityModel(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL);

        for (DyeColor color : DyeColor.values()) {
            ResourceLocation parasolLoc = MineraculousKamikotizationsItems.PARASOLS.get(color).id();
            ResourceLocation openLoc = parasolLoc.withSuffix("_open");
            parasol(parasolLoc, modLoc("item/parasol_closed"), modItemLoc(parasolLoc.getPath() + "_in_hand"))
                    .override()
                    .predicate(MineraculousItemProperties.ACTIVE, 1)
                    .model(parasol(openLoc, modLoc("item/parasol_open"), modItemLoc(openLoc.getPath() + "_in_hand")))
                    .end();
        }
    }

    private void registerLookTransforms() {
        transforms(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL, "in_hand")
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).translation(0, -4.5F, 1).scale(0.75F).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).translation(0, -4.5F, 1).scale(0.75F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).translation(0, -0.25F, 1).scale(0.5F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).translation(0, -0.25F, 1).scale(0.5F).end()
                .transform(ItemDisplayContext.HEAD).translation(0, 1.25F, 0).scale(0.75F).end()
                .end();
        transforms(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL, "open")
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).translation(0, -4.5F, 1).scale(0.75F).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).translation(0, -4.5F, 1).scale(0.75F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).translation(0, -0.25F, 1).scale(0.5F).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).translation(0, -0.25F, 1).scale(0.5F).end()
                .transform(ItemDisplayContext.HEAD).translation(0, -14.5F, 0).scale(0.75F).end()
                .end();
    }

    private ModelBuilder<ItemModelBuilder>.TransformsBuilder transforms(String name) {
        return getBuilder(name).transforms();
    }

    private ModelBuilder<ItemModelBuilder>.TransformsBuilder transforms(ItemLike item) {
        return transforms(item.asItem().builtInRegistryHolder().key().location().getPath());
    }

    private ModelBuilder<ItemModelBuilder>.TransformsBuilder transforms(ItemLike item, String suffix) {
        return transforms(item.asItem().builtInRegistryHolder().key().location().getPath() + "_" + suffix);
    }

    private ItemModelBuilder parasol(ResourceLocation loc, ResourceLocation parent, ResourceLocation inHandTexture, ResourceLocation guiLoc) {
        return withSeparateInventoryModel(loc, withExistingParent(inHandTexture.getPath(), parent)
                .texture("texture", inHandTexture)
                .texture("particle", inHandTexture), basicItem(guiLoc));
    }

    private ItemModelBuilder parasol(ResourceLocation loc, ResourceLocation parent, ResourceLocation inHandTexture) {
        return parasol(loc, parent, inHandTexture, loc);
    }
}
