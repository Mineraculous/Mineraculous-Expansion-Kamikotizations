package dev.thomasglasser.mineraculouskamikotizations.data.particles;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.core.particles.MineraculousKamikotizationsParticleTypes;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class MineraculousKamikotizationsParticleDescriptionProvider extends ParticleDescriptionProvider {
    public MineraculousKamikotizationsParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(MineraculousKamikotizationsParticleTypes.FLOATING_BUBBLE.get(),
                MineraculousKamikotizations.modLoc("bubble"),
                MineraculousKamikotizations.modLoc("bubble_pop_0"),
                MineraculousKamikotizations.modLoc("bubble_pop_1"),
                MineraculousKamikotizations.modLoc("bubble_pop_2"),
                MineraculousKamikotizations.modLoc("bubble_pop_3"),
                MineraculousKamikotizations.modLoc("bubble_pop_4"));
    }
}
