package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilitySource;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;

public abstract class AbstractMimicMiraculousAbilityAction implements AbilityAction {
    public static final CustomizationSettingAbilityProperty<TransformationHistoryData.TransformationEntry> POWER_TARGET = MineraculousKamikotizationsAbilityProperties.POWER_TARGET;
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(POWER_TARGET);
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of();

    @Override
    public ImmutableSet<AbilityProperty<?>> getRequiredProperties() {
        return REQUIRED_PROPERTIES;
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        return OPTIONAL_PROPERTIES;
    }

    protected Ability.Context createProxyContext(CustomizationData customizationData, Ability.Context context) {
        AbilitySource source = context.source();
        return new Ability.Context(context.level(), context.performer(), new AbilitySource(source.key(), customizationData.settings(), source.powerLevel(), source.nameColor(), source.powerActive(), source.storedEntities(), source.tool()), context.target());
    }
}
