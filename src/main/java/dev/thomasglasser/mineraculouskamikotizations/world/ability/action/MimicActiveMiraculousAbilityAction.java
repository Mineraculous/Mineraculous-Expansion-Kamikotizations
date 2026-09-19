package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicActiveMiraculousAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.Optional;

public class MimicActiveMiraculousAbilityAction extends AbstractMimicMiraculousAbilityAction {
    public static final CustomizationSettingAbilityProperty<Boolean> ENABLE_TIMER = MineraculousKamikotizationsAbilityProperties.ENABLE_TIMER;
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(MIRACULOUS, ENABLE_TIMER);

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        CustomizationSettings customizationSettings = context.source().customizationSettings();
        MimicActiveMiraculousAbilityState mimicMiraculousActiveAbilityState = PerformerAbilityData.get(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_ACTIVE_MIRACULOUS);
        TransformationHistoryData.TransformationEntry transformationEntry = customizationSettings.get(instance.getOptionalCustomizationSetting(TRANSFORMATION_ENTRY));
        if (mimicMiraculousActiveAbilityState != null) {
            if (customizationSettings.get(instance.getRequiredCustomizationSetting(ENABLE_TIMER))) {
                // TODO: make timer logic and stuff
            }
            // TODO: Tommy explain to me why this stopped working
            return AbilityUtils.performActiveAbility(Optional.of(customizationSettings.get(instance.getRequiredCustomizationSetting(MIRACULOUS)).value().activeAbility()), createProxyContext(mimicMiraculousActiveAbilityState.customizationData(), context));
        }
        return Ability.Result.fail();
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_ACTIVE_MIRACULOUS.get());
        super.detransform(instance, context);
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getRequiredProperties() {
        return REQUIRED_PROPERTIES;
    }

    @Override
    protected AbilityState createState(CustomizationData customizationData) {
        return new MimicActiveMiraculousAbilityState(customizationData);
    }
}
