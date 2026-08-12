package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettingType;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PlayerCustomizationSettingType implements CustomizationSettingType<UUID> {
    @Override
    public Codec<UUID> codec() {
        return UUIDUtil.CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, UUID> streamCodec() {
        return UUIDUtil.STREAM_CODEC.cast();
    }
}
