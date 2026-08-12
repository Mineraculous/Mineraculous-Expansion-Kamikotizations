package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

public class MineraculousKamikotizationsCustomizationSettingTypes {
    private static final DeferredRegister<CustomizationSettingType<?>> CUSTOMIZATION_SETTING_TYPES = DeferredRegister.create(MineraculousRegistries.CUSTOMIZATION_SETTING_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<CustomizationSettingType<?>, MiraculousCustomizationSettingType> MIRACULOUS = CUSTOMIZATION_SETTING_TYPES.register("miraculous", MiraculousCustomizationSettingType::new);

    public static final DeferredHolder<CustomizationSettingType<?>, PlayerCustomizationSettingType> PLAYER = CUSTOMIZATION_SETTING_TYPES.register("player", PlayerCustomizationSettingType::new);

    @ApiStatus.Internal
    public static void init() {}
}
