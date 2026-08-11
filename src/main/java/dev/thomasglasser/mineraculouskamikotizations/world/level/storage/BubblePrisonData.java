package dev.thomasglasser.mineraculouskamikotizations.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.BubblePrison;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BubblePrisonData(BubblePrison.BubbleBehavior behavior, double scaleFactor, List<UUID> captiveEntities, float health) {

    public static final Codec<BubblePrisonData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BubblePrison.BubbleBehavior.CODEC.fieldOf("behavior").forGetter(BubblePrisonData::behavior),
            Codec.DOUBLE.fieldOf("scale_factor").forGetter(BubblePrisonData::scaleFactor),
            UUIDUtil.CODEC.listOf().fieldOf("captive_entities").forGetter(BubblePrisonData::captiveEntities),
            Codec.FLOAT.fieldOf("health").forGetter(BubblePrisonData::health)).apply(instance, BubblePrisonData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BubblePrisonData> STREAM_CODEC = StreamCodec.composite(
            BubblePrison.BubbleBehavior.STREAM_CODEC, BubblePrisonData::behavior,
            ByteBufCodecs.DOUBLE, BubblePrisonData::scaleFactor,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), BubblePrisonData::captiveEntities,
            ByteBufCodecs.FLOAT, BubblePrisonData::health,
            BubblePrisonData::new);
    public BubblePrisonData() {
        this(BubblePrison.BubbleBehavior.CAPTURING, 1, List.of(), 100);
    }

    public BubblePrisonData withBehavior(BubblePrison.BubbleBehavior behavior) {
        return new BubblePrisonData(behavior, scaleFactor, captiveEntities, health);
    }

    public BubblePrisonData withScaleFactor(double scaleFactor) {
        return new BubblePrisonData(behavior, scaleFactor, captiveEntities, health);
    }

    public BubblePrisonData withCaptiveEntities(List<UUID> captiveEntities) {
        return new BubblePrisonData(behavior, scaleFactor, captiveEntities, health);
    }

    public BubblePrisonData withHealth(float health) {
        return new BubblePrisonData(behavior, scaleFactor, captiveEntities, health);
    }
}
