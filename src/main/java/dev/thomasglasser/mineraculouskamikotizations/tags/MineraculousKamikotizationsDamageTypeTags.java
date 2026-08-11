package dev.thomasglasser.mineraculouskamikotizations.tags;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class MineraculousKamikotizationsDamageTypeTags {
    public static final TagKey<DamageType> BREAKS_BUBBLE_PRISONS = create("breaks_bubble_prisons");

    private static TagKey<DamageType> create(String id) {
        return TagKey.create(Registries.DAMAGE_TYPE, MineraculousKamikotizations.modLoc(id));
    }
}
