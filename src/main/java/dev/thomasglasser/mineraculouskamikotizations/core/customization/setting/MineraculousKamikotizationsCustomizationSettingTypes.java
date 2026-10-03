package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.customization.setting.SimpleCustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.targetvisual.VisualTarget;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;
import net.minecraft.network.codec.ByteBufCodecs;

public class MineraculousKamikotizationsCustomizationSettingTypes {
    private static final Registrar<CustomizationSettingType<?>> CUSTOMIZATION_SETTING_TYPES = Registrar.create(MineraculousRegistries.CUSTOMIZATION_SETTING_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final ExtendedHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<Either<VisualTarget, TransformationHistoryData.TransformationEntry>>> VISUAL_TARGET = CUSTOMIZATION_SETTING_TYPES.register("visual_target", () -> new SimpleCustomizationSettingType<>(Codec.either(VisualTarget.CODEC, TransformationHistoryData.TransformationEntry.CODEC), ByteBufCodecs.either(VisualTarget.STREAM_CODEC, TransformationHistoryData.TransformationEntry.STREAM_CODEC)));

    public static final ExtendedHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<TransformationHistoryData.TransformationEntry>> POWER_TARGET = CUSTOMIZATION_SETTING_TYPES.register("power_target", () -> new SimpleCustomizationSettingType<>(TransformationHistoryData.TransformationEntry.CODEC, TransformationHistoryData.TransformationEntry.STREAM_CODEC));

    public static final ExtendedHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<Boolean>> BOOLEAN = CUSTOMIZATION_SETTING_TYPES.register("boolean", () -> new SimpleCustomizationSettingType<>(Codec.BOOL, ByteBufCodecs.BOOL.cast()));

    public static void init() {}
}
