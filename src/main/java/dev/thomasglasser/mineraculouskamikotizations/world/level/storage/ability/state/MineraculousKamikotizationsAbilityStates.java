package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;

public class MineraculousKamikotizationsAbilityStates {
    private static final DeferredRegister<AbilityState.Type<?>> ABILITY_STATES = DeferredRegister.create(MineraculousRegistries.ABILITY_STATE, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<MimicActiveMiraculousAbilityState>> MIMIC_ACTIVE_MIRACULOUS = ABILITY_STATES.register("mimic_active_miraculous", () -> MimicActiveMiraculousAbilityState.TYPE);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<MimicPassiveMiraculousAbilityState>> MIMIC_PASSIVE_MIRACULOUS = ABILITY_STATES.register("mimic_passive_miraculous", () -> MimicPassiveMiraculousAbilityState.TYPE);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<MimicVisualMiraculousAbilityState>> MIMIC_VISUAL_MIRACULOUS = ABILITY_STATES.register("mimic_visual_miraculous", () -> MimicVisualMiraculousAbilityState.TYPE);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<MimicMiraculousNameColorAbilityState>> MIMIC_MIRACULOUS_NAME_COLOR = ABILITY_STATES.register("mimic_miraculous_name_color", () -> MimicMiraculousNameColorAbilityState.TYPE);

    public static final DeferredHolder<AbilityState.Type<?>, AbilityState.Type<MimicMiraculousNameAbilityState>> MIMIC_MIRACULOUS_NAME = ABILITY_STATES.register("mimic_miraculous_name", () -> MimicMiraculousNameAbilityState.TYPE);

    public static void init() {}
}
