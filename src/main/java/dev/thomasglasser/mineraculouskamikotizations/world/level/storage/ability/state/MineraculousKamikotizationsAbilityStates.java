package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;

public class MineraculousKamikotizationsAbilityStates {
    private static final DeferredRegister<AbilityState.Type<?>> ABILITY_STATES = DeferredRegister.create(MineraculousRegistries.ABILITY_STATE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<ActiveMiraculousMimicAbilityState>> ACTIVE_MIRACULOUS_MIMIC = ABILITY_STATES.register("active_miraculous_mimic", () -> ActiveMiraculousMimicAbilityState.TYPE);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<PassiveMiraculousMimicAbilityState>> PASSIVE_MIRACULOUS_MIMIC = ABILITY_STATES.register("passive_miraculous_mimic", () -> PassiveMiraculousMimicAbilityState.TYPE);

    public static void init() {}
}
