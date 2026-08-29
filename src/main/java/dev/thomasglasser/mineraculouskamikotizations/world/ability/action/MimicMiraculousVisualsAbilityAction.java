package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.VisualMiraculousMimicAbilityState;

public class MimicMiraculousVisualsAbilityAction extends AbstractMimicMiraculousAbilityAction {
    public static final CustomizationSettingAbilityProperty<Boolean> LOOKS = MineraculousKamikotizationsAbilityProperties.LOOKS;
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(LOOKS);

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        return Ability.Result.fail();
    }

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        super.transform(instance, context);
        // TODO: Looks logic
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.VISUAL_MIRACULOUS_MIMIC.get());
        super.detransform(instance, context);
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        return OPTIONAL_PROPERTIES;
    }

    @Override
    protected AbilityState createState(CustomizationData customizationData) {
        return new VisualMiraculousMimicAbilityState(customizationData);
    }
}
