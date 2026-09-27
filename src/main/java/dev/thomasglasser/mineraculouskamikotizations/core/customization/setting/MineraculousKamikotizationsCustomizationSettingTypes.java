package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.customization.setting.SimpleCustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class MineraculousKamikotizationsCustomizationSettingTypes {
    private static final DeferredRegister<CustomizationSettingType<?>> CUSTOMIZATION_SETTING_TYPES = DeferredRegister.create(MineraculousRegistries.CUSTOMIZATION_SETTING_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>>> VISUAL_TARGET = CUSTOMIZATION_SETTING_TYPES.register("visual_target", () -> new SimpleCustomizationSettingType<>(Codec.either(RecordCodecBuilder.create(instance -> instance.group(Miraculous.CODEC.fieldOf("miraculous").forGetter(Pair::getFirst), UUIDUtil.CODEC.fieldOf("uuid").forGetter(Pair::getSecond)).apply(instance, Pair::of)), TransformationHistoryData.TransformationEntry.CODEC), ByteBufCodecs.either(StreamCodec.composite(Miraculous.STREAM_CODEC, Pair::getFirst, UUIDUtil.STREAM_CODEC, Pair::getSecond, Pair::of), TransformationHistoryData.TransformationEntry.STREAM_CODEC)));

    public static final DeferredHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<TransformationHistoryData.TransformationEntry>> POWER_TARGET = CUSTOMIZATION_SETTING_TYPES.register("power_target", () -> new SimpleCustomizationSettingType<>(TransformationHistoryData.TransformationEntry.CODEC, TransformationHistoryData.TransformationEntry.STREAM_CODEC));

    public static final DeferredHolder<CustomizationSettingType<?>, SimpleCustomizationSettingType<Boolean>> BOOLEAN = CUSTOMIZATION_SETTING_TYPES.register("boolean", () -> new SimpleCustomizationSettingType<>(Codec.BOOL, ByteBufCodecs.BOOL.cast()));

    public static void init() {}
}
