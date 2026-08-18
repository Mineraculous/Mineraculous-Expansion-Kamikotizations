package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;

public class MineraculousKamikotizationsAbilityStates {
    private static final DeferredRegister<AbilityState.Type<?>> ABILITY_STATES = DeferredRegister.create(MineraculousRegistries.ABILITY_STATE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<MimicMiraculousAbilityState>> MIMIC_MIRACULOUS = ABILITY_STATES.register("mimic_miraculous", () -> MimicMiraculousAbilityState.TYPE);

    public static void init() {}
}
