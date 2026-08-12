package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettingType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class PlayerCustomizationSettingType implements CustomizationSettingType<String> {
    public static final Codec<String> CODEC = Codec.STRING;
    public static final StreamCodec<RegistryFriendlyByteBuf, String> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.cast();

    @Override
    public Codec<String> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, String> streamCodec() {
        return STREAM_CODEC;
    }
}
