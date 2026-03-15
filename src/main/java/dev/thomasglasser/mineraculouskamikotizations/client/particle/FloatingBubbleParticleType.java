package dev.thomasglasser.mineraculouskamikotizations.client.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class FloatingBubbleParticleType extends ParticleType<FloatingBubbleParticleOptions> {
    public FloatingBubbleParticleType() {
        super(false);
    }

    @Override
    public MapCodec<FloatingBubbleParticleOptions> codec() {
        return FloatingBubbleParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FloatingBubbleParticleOptions> streamCodec() {
        return FloatingBubbleParticleOptions.STREAM_CODEC;
    }
}
