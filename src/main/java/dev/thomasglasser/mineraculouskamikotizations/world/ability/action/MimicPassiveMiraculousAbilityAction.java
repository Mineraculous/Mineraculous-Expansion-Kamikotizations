package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicPassiveMiraculousAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;

public class MimicPassiveMiraculousAbilityAction extends AbstractMimicMiraculousAbilityAction {
    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        MimicPassiveMiraculousAbilityState passiveMiraculousMimicAbilityState = PerformerAbilityData.get(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_PASSIVE_MIRACULOUS);
        TransformationHistoryData.TransformationEntry transformationEntry = context.source().customizationSettings().get(instance.getRequiredCustomizationSetting(POWER_TARGET));
        if (passiveMiraculousMimicAbilityState != null && transformationEntry != null) {
            return AbilityUtils.performPassiveAbilities(transformationEntry.miraculous().value().passiveAbilities(), createProxyContext(passiveMiraculousMimicAbilityState.customizationData(), context));
        }
        return Ability.Result.pass();
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_PASSIVE_MIRACULOUS.get());
        super.detransform(instance, context);
    }

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        super.transform(instance, context);
        TransformationHistoryData.TransformationEntry entry = context.source().customizationSettings().get(instance.getRequiredCustomizationSetting(POWER_TARGET));
        if (entry != null) {
            PerformerAbilityData.add(context.performer(), new MimicPassiveMiraculousAbilityState(entry.customizationData().orElse(CustomizationData.EMPTY)));
        }
    }
}
