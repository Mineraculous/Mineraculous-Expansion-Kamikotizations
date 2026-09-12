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
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import net.minecraft.core.Holder;

public abstract class AbstractMimicMiraculousAbilityAction implements AbilityAction {
    public static final CustomizationSettingAbilityProperty<Holder<Miraculous>> MIRACULOUS = MineraculousKamikotizationsAbilityProperties.MIRACULOUS;
    public static final CustomizationSettingAbilityProperty<TransformationHistoryData.TransformationEntry> TRANSFORMATION_ENTRY = MineraculousKamikotizationsAbilityProperties.TARGET;
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(MIRACULOUS);
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(TRANSFORMATION_ENTRY);

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        AbilitySource source = context.source();
        CustomizationSettings customizationSettings = source.customizationSettings();
        TransformationHistoryData.TransformationEntry transformationEntry = customizationSettings.get(instance.getOptionalCustomizationSetting(TRANSFORMATION_ENTRY));
        PerformerAbilityData.add(context.performer(), createState(transformationEntry != null ? transformationEntry.customizationData().orElse(CustomizationData.EMPTY) : CustomizationData.EMPTY));
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getRequiredProperties() {
        return REQUIRED_PROPERTIES;
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        return OPTIONAL_PROPERTIES;
    }

    protected abstract AbilityState createState(CustomizationData customizationData);

    protected Ability.Context createProxyContext(CustomizationData customizationData, Ability.Context context) {
        AbilitySource source = context.source();
        return new Ability.Context(context.level(), context.performer(), new AbilitySource(source.key(), customizationData.settings(), source.powerLevel(), source.nameColor(), source.powerActive(), source.storedEntities(), source.tool()), context.target());
    }
}
