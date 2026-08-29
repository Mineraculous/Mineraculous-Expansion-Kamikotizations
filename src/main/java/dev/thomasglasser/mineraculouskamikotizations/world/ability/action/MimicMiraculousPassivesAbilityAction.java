package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.PassiveMiraculousMimicAbilityState;

public class MimicMiraculousPassivesAbilityAction extends AbstractMimicMiraculousAbilityAction {
    @Override
    protected AbilityState createState(CustomizationData customizationData) {
        return new PassiveMiraculousMimicAbilityState(customizationData);
    }

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PassiveMiraculousMimicAbilityState passiveMiraculousMimicAbilityState = PerformerAbilityData.get(context.performer(), MineraculousKamikotizationsAbilityStates.PASSIVE_MIRACULOUS_MIMIC);
        if (passiveMiraculousMimicAbilityState != null) {
            return AbilityUtils.performPassiveAbilities(context.source().customizationSettings().get(instance.getRequiredCustomizationSetting(MIRACULOUS)).value().passiveAbilities(), createProxyContext(passiveMiraculousMimicAbilityState.customizationData(), context));
        }
        return Ability.Result.fail();
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.PASSIVE_MIRACULOUS_MIMIC.get());
        super.detransform(instance, context);
    }
}
