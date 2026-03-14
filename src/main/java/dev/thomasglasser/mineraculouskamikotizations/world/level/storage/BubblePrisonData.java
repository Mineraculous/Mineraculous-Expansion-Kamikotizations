package dev.thomasglasser.mineraculouskamikotizations.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BubblePrisonData(UUID bubbleUUID, int bubbleID) {
    public static final Codec<BubblePrisonData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("bubble_uuid").forGetter(BubblePrisonData::bubbleUUID),
            Codec.INT.fieldOf("bubble_id").forGetter(BubblePrisonData::bubbleID)).apply(instance, BubblePrisonData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BubblePrisonData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, BubblePrisonData::bubbleUUID,
            ByteBufCodecs.INT, BubblePrisonData::bubbleID,
            BubblePrisonData::new);
}
