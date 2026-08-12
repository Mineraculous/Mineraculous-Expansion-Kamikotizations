package dev.thomasglasser.mineraculouskamikotizations.world.ability.property;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.HolderAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import java.util.UUID;

public class MineraculousKamikotizationsAbilityProperties {
    public static final HolderAbilityProperty<Miraculous> MIRACULOUS = new HolderAbilityProperty<>("miraculous", MineraculousRegistries.MIRACULOUS);
    public static final AbilityProperty<UUID> PLAYER = new PlayerAbilityProperty("player");
}
