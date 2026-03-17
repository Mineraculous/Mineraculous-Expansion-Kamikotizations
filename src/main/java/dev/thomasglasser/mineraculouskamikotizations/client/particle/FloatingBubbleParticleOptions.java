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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    public static final Vec3 RED = new Vec3(1.0, 0.247, 0.0);
    public static final Vec3 GREEN = new Vec3(0.341, 1.0, 0.69);

    public static void spawnRed(Level level, Vec3 pos, int count, boolean randomness) {
        spawn(level, pos, RED, count, randomness);
    }

    public static void spawnGreen(Level level, Vec3 pos, int count, boolean randomness) {
        spawn(level, pos, GREEN, count, randomness);
    }

    public static void spawn(Level level, Vec3 pos, Vec3 color, int count, boolean randomness) {
        FloatingBubbleParticleOptions options = new FloatingBubbleParticleOptions((float) color.x, (float) color.y, (float) color.z, 120, true);
        for (int i = 0; i < count; i++) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(options,
                        pos.x + (randomness ? Math.random() : 0),
                        pos.y + (randomness ? Math.random() : 0),
                        pos.z + (randomness ? Math.random() : 0),
                        count, 0.1, 0.1, 0.1, 0.02);
            } else {
                //level.addParticle(options, pos.x, pos.y, pos.z, 0, 0, 0);
            }
        }
    }
}
