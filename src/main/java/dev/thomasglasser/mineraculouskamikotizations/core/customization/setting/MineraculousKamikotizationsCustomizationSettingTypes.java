package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.customization.setting.HolderCustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.customization.setting.SimpleCustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;

public class MineraculousKamikotizationsCustomizationSettingTypes {
    private static final DeferredRegister<CustomizationSettingType<?>> CUSTOMIZATION_SETTING_TYPES = DeferredRegister.create(MineraculousRegistries.CUSTOMIZATION_SETTING_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<CustomizationSettingType<?>, HolderCustomizationSettingType<Miraculous>> MIRACULOUS = CUSTOMIZATION_SETTING_TYPES.register("miraculous", () -> new HolderCustomizationSettingType<>(MineraculousRegistries.MIRACULOUS, Miraculous.CODEC, Miraculous.STREAM_CODEC));

    public static final DeferredHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<UUID>> PLAYER = CUSTOMIZATION_SETTING_TYPES.register("player", () -> new SimpleCustomizationSettingType<>(UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC.cast()));

    public static final DeferredHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<Boolean>> TIMER = CUSTOMIZATION_SETTING_TYPES.register("timer", () -> new SimpleCustomizationSettingType<>(Codec.BOOL, ByteBufCodecs.BOOL.cast()));

    public static void init() {}
}
