package dev.thomasglasser.mineraculouskamikotizations.data.look;

import dev.thomasglasser.mineraculous.api.client.look.asset.LookAssetTypes;
import dev.thomasglasser.mineraculous.api.client.renderer.item.MineraculousItemProperties;
import dev.thomasglasser.mineraculous.api.core.look.LookUtils;
import dev.thomasglasser.mineraculous.api.core.look.context.LookContexts;
import dev.thomasglasser.mineraculous.impl.data.looks.MineraculousLookProvider;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization.MineraculousKamikotizationsKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.item.WeatherControlParasolItem;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class MineraculousKamikotizationsLookProvider extends MineraculousLookProvider {
    public MineraculousKamikotizationsLookProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, MineraculousKamikotizations.MOD_ID, lookupProvider);
    }

    @Override
    protected void registerLooks(HolderLookup.Provider provider) {
        String closedTexture = modString("textures/item/geo/weather_control_parasol.png");
        String closedGuiTexture = modString("textures/item/weather_control_parasol.png");

        kamikotizationLook(MineraculousKamikotizationsKamikotizations.WEATHER_CONTROL, LookUtils.DEFAULT_NAME)
                .context(LookContexts.KAMIKOTIZATION_TOOL)
                .assets()
                .add(LookAssetTypes.TEXTURE, closedTexture)
                .add(LookAssetTypes.GECKOLIB_MODEL, modString("geo/item/weather_control_parasol.geo.json"))
                .add(LookAssetTypes.ITEM_TRANSFORMS, modString("models/item/weather_control_parasol_in_hand.json"))
                .add(LookAssetTypes.FLAT_TEXTURE, closedGuiTexture)
                .end()
                .override()
                .predicate(MineraculousItemProperties.MODE, MineraculousItemProperties.getPropertyForEnum(WeatherControlParasolItem.Mode.ICE))
                .assets()
                .add(LookAssetTypes.TEXTURE, modString("textures/item/geo/weather_control_parasol_ice.png"))
                .add(LookAssetTypes.FLAT_TEXTURE, modString("textures/item/weather_control_parasol_ice.png"))
                .end()
                .end()
                .override()
                .predicate(MineraculousItemProperties.MODE, MineraculousItemProperties.getPropertyForEnum(WeatherControlParasolItem.Mode.ICE) + 1)
                .assets()
                .add(LookAssetTypes.TEXTURE, closedTexture)
                .add(LookAssetTypes.FLAT_TEXTURE, closedGuiTexture)
                .end()
                .end()
                .override()
                .predicate(MineraculousItemProperties.ACTIVE, 1)
                .assets()
                .add(LookAssetTypes.TEXTURE, modString("textures/item/geo/weather_control_parasol_open.png"))
                .add(LookAssetTypes.GECKOLIB_MODEL, modString("geo/item/weather_control_parasol_open.geo.json"))
                .add(LookAssetTypes.ITEM_TRANSFORMS, modString("models/item/weather_control_parasol_open.json"))
                .add(LookAssetTypes.FLAT_TEXTURE, modString("textures/item/weather_control_parasol_open.png"));
    }
}
