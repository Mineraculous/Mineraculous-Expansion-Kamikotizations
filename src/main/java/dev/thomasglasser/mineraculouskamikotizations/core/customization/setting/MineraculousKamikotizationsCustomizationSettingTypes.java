package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.customization.setting.SimpleCustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import org.jetbrains.annotations.ApiStatus;

public class MineraculousKamikotizationsCustomizationSettingTypes {
    private static final DeferredRegister<CustomizationSettingType<?>> CUSTOMIZATION_SETTING_TYPES = DeferredRegister.create(MineraculousRegistries.CUSTOMIZATION_SETTING_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<Holder<Miraculous>>> MIRACULOUS = CUSTOMIZATION_SETTING_TYPES.register("miraculous", () -> new SimpleCustomizationSettingType<>(Miraculous.CODEC, Miraculous.STREAM_CODEC));

    public static final DeferredHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<UUID>> PLAYER = CUSTOMIZATION_SETTING_TYPES.register("player", () -> new SimpleCustomizationSettingType<>(UUIDUtil.LENIENT_CODEC, UUIDUtil.STREAM_CODEC.cast()));

    @ApiStatus.Internal
    public static void init() {}
}
