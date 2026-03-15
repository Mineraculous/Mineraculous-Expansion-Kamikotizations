package dev.thomasglasser.mineraculouskamikotizations.core.particles;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.client.particle.FloatingBubbleParticleType;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;

public class MineraculousKamikotizationsParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, FloatingBubbleParticleType> FLOATING_BUBBLE = PARTICLE_TYPES.register("floating_bubble", FloatingBubbleParticleType::new);

    public static void init() {}
}
