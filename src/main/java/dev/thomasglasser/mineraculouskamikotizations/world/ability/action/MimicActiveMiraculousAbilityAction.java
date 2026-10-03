package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicActiveMiraculousAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.Optional;

public class MimicActiveMiraculousAbilityAction extends AbstractMimicMiraculousAbilityAction {
    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        MimicActiveMiraculousAbilityState state = PerformerAbilityData.get(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_ACTIVE_MIRACULOUS);
        TransformationHistoryData.TransformationEntry entry = context.source().customizationSettings().getOrDefault(instance.getRequiredCustomizationSetting(POWER_TARGET));
        if (state != null) {
            return AbilityUtils.performActiveAbility(Optional.of(entry.miraculous().value().activeAbility()), createProxyContext(state.customizationData(), context));
        }
        return Ability.Result.pass();
    }

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        super.transform(instance, context);
        TransformationHistoryData.TransformationEntry entry = context.source().customizationSettings().getOrDefault(instance.getRequiredCustomizationSetting(POWER_TARGET));
        PerformerAbilityData.add(context.performer(), new MimicActiveMiraculousAbilityState(entry.customizationData().orElse(CustomizationData.EMPTY)));
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_ACTIVE_MIRACULOUS.get());
        super.detransform(instance, context);
    }
}
