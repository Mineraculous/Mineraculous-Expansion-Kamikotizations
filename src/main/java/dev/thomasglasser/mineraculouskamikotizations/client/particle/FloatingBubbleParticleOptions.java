package dev.thomasglasser.mineraculouskamikotizations.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.mineraculouskamikotizations.core.particles.MineraculousKamikotizationsParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FloatingBubbleParticleOptions(float r, float g, float b, int lifetime, boolean limitedShift) implements ParticleOptions {

    public static final MapCodec<FloatingBubbleParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(FloatingBubbleParticleOptions::r),
            Codec.FLOAT.fieldOf("g").forGetter(FloatingBubbleParticleOptions::g),
            Codec.FLOAT.fieldOf("b").forGetter(FloatingBubbleParticleOptions::b),
            Codec.INT.fieldOf("lifetime").forGetter(FloatingBubbleParticleOptions::lifetime),
            Codec.BOOL.fieldOf("limited_shift").forGetter(FloatingBubbleParticleOptions::limitedShift) // Added this
    ).apply(instance, FloatingBubbleParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FloatingBubbleParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, FloatingBubbleParticleOptions::r,
            ByteBufCodecs.FLOAT, FloatingBubbleParticleOptions::g,
            ByteBufCodecs.FLOAT, FloatingBubbleParticleOptions::b,
            ByteBufCodecs.VAR_INT, FloatingBubbleParticleOptions::lifetime,
            ByteBufCodecs.BOOL, FloatingBubbleParticleOptions::limitedShift,
            FloatingBubbleParticleOptions::new);
    @Override
    public ParticleType<?> getType() {
        return MineraculousKamikotizationsParticleTypes.FLOATING_BUBBLE.get();
    }
}
