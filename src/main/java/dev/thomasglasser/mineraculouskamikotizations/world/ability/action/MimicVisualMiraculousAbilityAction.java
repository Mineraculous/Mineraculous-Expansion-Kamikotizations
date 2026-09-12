package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilitySource;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameColorAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicVisualMiraculousAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;

public class MimicVisualMiraculousAbilityAction extends AbstractMimicMiraculousAbilityAction {
    public static final CustomizationSettingAbilityProperty<Boolean> LOOKS = MineraculousKamikotizationsAbilityProperties.COPY_LOOKS;
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_NAME_COLOR = MineraculousKamikotizationsAbilityProperties.COPY_NAME_COLOR;
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(LOOKS);
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(COPY_NAME_COLOR);

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        return Ability.Result.fail();
    }

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        super.transform(instance, context);
        AbilitySource source = context.source();
        CustomizationSettings customizationSettings = source.customizationSettings();
        TransformationHistoryData.TransformationEntry transformationEntry = customizationSettings.get(instance.getOptionalCustomizationSetting(TRANSFORMATION_ENTRY));
        if (customizationSettings.get(instance.getRequiredCustomizationSetting(COPY_NAME_COLOR)) && transformationEntry != null)
            PerformerAbilityData.add(context.performer(), new MimicMiraculousNameColorAbilityState(transformationEntry.miraculous().value().color().getValue()));
            PerformerAbilityData.add(context.performer(), new MimicMiraculousNameColorAbilityState(transformationEntry.miraculous().value().color().getValue()));
        }
        // TODO: Looks logic
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_VISUAL_MIRACULOUS.get());
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME_COLOR.get());
        super.detransform(instance, context);
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        return OPTIONAL_PROPERTIES;
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getRequiredProperties() {
        return REQUIRED_PROPERTIES;
    }

    @Override
    protected AbilityState createState(CustomizationData customizationData) {
        return new MimicVisualMiraculousAbilityState(customizationData);
    }
}
