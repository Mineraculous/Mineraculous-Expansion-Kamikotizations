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
            withSeparateInventoryModel(parasolLoc, withExistingParent(parasolLoc.getPath(), modLoc("item/parasol_closed"))
                    .texture("texture", parasolLoc.withPrefix("item/"))
                    .texture("particle", parasolLoc.withPrefix("item/")), basicItem(parasolLoc.withSuffix("_inventory")))
                            .override()
                            .predicate(MineraculousItemProperties.ACTIVE, 1)
                            .model(withSeparateInventoryModel(openLoc, withExistingParent(openLoc.getPath(), modLoc("item/parasol_open"))
                                    .texture("texture", openLoc.withPrefix("item/"))
                                    .texture("particle", openLoc.withPrefix("item/")), basicItem(openLoc.withSuffix("_inventory"))))
                            .end();
        }

        ResourceLocation parasolLoc = MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.getId();
        ResourceLocation iceLoc = parasolLoc.withSuffix("_ice");
        ResourceLocation activeLoc = parasolLoc.withSuffix("_active");
        ResourceLocation openLoc = parasolLoc.withSuffix("_open");
        ResourceLocation inactiveLoc = parasolLoc.withSuffix("_inactive");
        ItemModelBuilder inactiveParasol = withSeparateInventoryModel(inactiveLoc, withExistingParent(parasolLoc.getPath(), modLoc("item/weather_control_parasol_closed"))
                .texture("texture", parasolLoc.withPrefix("item/"))
                .texture("particle", parasolLoc.withPrefix("item/")), basicItem(parasolLoc.withSuffix("_inventory")));
        withExistingParent(parasolLoc.getPath(), inactiveLoc)
                .override()
                .predicate(MineraculousItemProperties.MODE, MineraculousItemProperties.getPropertyForEnum(WeatherControlParasolItem.Mode.ICE))
                .model(withSeparateInventoryModel(iceLoc, withExistingParent(iceLoc.getPath(), modLoc("item/weather_control_parasol_closed"))
                        .texture("texture", iceLoc.withPrefix("item/"))
                        .texture("particle", iceLoc.withPrefix("item/")), basicItem(iceLoc.withSuffix("_inventory"))))
                .end()
                .override()
                .predicate(MineraculousItemProperties.MODE, MineraculousItemProperties.getPropertyForEnum(WeatherControlParasolItem.Mode.ICE) + 1)
                .model(inactiveParasol)
                .end()
                .override()
                .predicate(MineraculousItemProperties.ACTIVE, 1)
                .model(withSeparateInventoryModel(activeLoc, withExistingParent(activeLoc.getPath(), openLoc.withPrefix("item/"))
                        .texture("texture", openLoc.withPrefix("item/"))
                        .texture("particle", openLoc.withPrefix("item/")), basicItem(openLoc.withSuffix("_inventory"))))
                .end();
    }
}
