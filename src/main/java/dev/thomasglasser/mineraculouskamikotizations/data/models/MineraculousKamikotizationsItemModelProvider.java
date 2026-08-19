package dev.thomasglasser.mineraculouskamikotizations.data.models;

import dev.thomasglasser.mineraculous.api.client.renderer.item.MineraculousItemProperties;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import dev.thomasglasser.mineraculouskamikotizations.world.item.WeatherControlParasolItem;
import dev.thomasglasser.tommylib.api.data.models.ExtendedItemModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
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
            parasol(parasolLoc, modLoc("item/parasol_closed"), modItemLoc(parasolLoc.getPath() + "_in_hand"))
                    .override()
                    .predicate(MineraculousItemProperties.ACTIVE, 1)
                    .model(parasol(openLoc, modLoc("item/parasol_open"), modItemLoc(openLoc.getPath() + "_in_hand")))
                    .end();
        }

        ResourceLocation parasolLoc = MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.getId();
        ResourceLocation iceLoc = parasolLoc.withSuffix("_ice");
        ResourceLocation openLoc = parasolLoc.withSuffix("_open");
        ItemModelBuilder closedModel = parasol(parasolLoc.withSuffix("_closed"), modLoc("item/weather_control_parasol_closed_base"), modItemLoc(parasolLoc.getPath() + "_in_hand"), parasolLoc);
        getBuilder(parasolLoc.getPath())
                .parent(closedModel)
                .override()
                .predicate(MineraculousItemProperties.MODE, MineraculousItemProperties.getPropertyForEnum(WeatherControlParasolItem.Mode.ICE))
                .model(parasol(iceLoc, modLoc("item/weather_control_parasol_closed_base"), modItemLoc(iceLoc.getPath() + "_in_hand")))
                .end()
                .override()
                .predicate(MineraculousItemProperties.MODE, MineraculousItemProperties.getPropertyForEnum(WeatherControlParasolItem.Mode.ICE) + 1)
                .model(closedModel)
                .end()
                .override()
                .predicate(MineraculousItemProperties.ACTIVE, 1)
                .model(parasol(openLoc, modLoc("item/weather_control_parasol_open_base"), modItemLoc(openLoc.getPath() + "_in_hand")))
                .end();
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
