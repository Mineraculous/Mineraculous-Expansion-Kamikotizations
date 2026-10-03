package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;

public class MineraculousKamikotizationsAbilityActions {
    private static final Registrar<AbilityAction> ACTIONS = Registrar.create(MineraculousRegistries.ABILITY_ACTION, MineraculousKamikotizations.MOD_ID);

    public static final ExtendedHolder<AbilityAction, MimicActiveMiraculousAbilityAction> MIMIC_ACTIVE_MIRACULOUS = ACTIONS.register("mimic_active_miraculous", MimicActiveMiraculousAbilityAction::new);
    public static final ExtendedHolder<AbilityAction, MimicPassiveMiraculousAbilityAction> MIMIC_PASSIVE_MIRACULOUS = ACTIONS.register("mimic_passive_miraculous", MimicPassiveMiraculousAbilityAction::new);
    public static final ExtendedHolder<AbilityAction, MimicVisualMiraculousAbilityAction> MIMIC_VISUAL_MIRACULOUS = ACTIONS.register("mimic_visual_miraculous", MimicVisualMiraculousAbilityAction::new);

    public static void init() {}
}
