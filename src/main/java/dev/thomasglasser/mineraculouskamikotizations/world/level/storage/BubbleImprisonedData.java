package dev.thomasglasser.mineraculouskamikotizations.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BubbleImprisonedData(UUID bubbleUUID, int bubbleID) {
    public static final Codec<BubbleImprisonedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("bubble_uuid").forGetter(BubbleImprisonedData::bubbleUUID),
            Codec.INT.fieldOf("bubble_id").forGetter(BubbleImprisonedData::bubbleID)).apply(instance, BubbleImprisonedData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BubbleImprisonedData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, BubbleImprisonedData::bubbleUUID,
            ByteBufCodecs.INT, BubbleImprisonedData::bubbleID,
            BubbleImprisonedData::new);
}
