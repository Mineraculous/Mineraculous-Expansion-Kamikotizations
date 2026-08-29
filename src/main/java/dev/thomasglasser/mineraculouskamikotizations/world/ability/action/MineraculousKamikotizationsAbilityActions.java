package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;

public class MineraculousKamikotizationsAbilityActions {
    private static final DeferredRegister<AbilityAction> ACTIONS = DeferredRegister.create(MineraculousRegistries.ABILITY_ACTION, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<AbilityAction, MimicMiraculousActiveAbilityAction> MIMIC_MIRACULOUS_ACTIVE = ACTIONS.register("mimic_miraculous_active", MimicMiraculousActiveAbilityAction::new);
    public static final DeferredHolder<AbilityAction, MimicMiraculousPassivesAbilityAction> MIMIC_MIRACULOUS_PASSIVES = ACTIONS.register("mimic_miraculous_passives", MimicMiraculousPassivesAbilityAction::new);
    public static final DeferredHolder<AbilityAction, MimicMiraculousVisualsAbilityAction> MIMIC_MIRACULOUS_VISUALS = ACTIONS.register("mimic_miraculous_visuals", MimicMiraculousVisualsAbilityAction::new);

    public static void init() {}
}
