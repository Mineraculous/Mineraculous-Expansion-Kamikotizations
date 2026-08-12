package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;

public class MineraculousKamikotizationsAbilityActions {
    private static final DeferredRegister<AbilityAction> ACTIONS = DeferredRegister.create(MineraculousRegistries.ABILITY_ACTION, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<AbilityAction, MimicActiveAbilityAction> REPLICATE_ACTIVE = ACTIONS.register("replicate_active", MimicActiveAbilityAction::new);

    public static void init() {}
}
