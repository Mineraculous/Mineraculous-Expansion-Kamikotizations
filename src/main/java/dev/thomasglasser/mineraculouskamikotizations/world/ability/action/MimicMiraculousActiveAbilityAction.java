package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousActiveAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

public class MimicMiraculousActiveAbilityAction extends AbstractMimicMiraculousAbilityAction {
    public static final CustomizationSettingAbilityProperty<Boolean> TIMER = MineraculousKamikotizationsAbilityProperties.TIMER;

    @Override
    protected AbilityState createState(CustomizationData customizationData) {
        return new MimicMiraculousActiveAbilityState(customizationData);
    }

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        Holder<Miraculous> miraculousHolder = context.source().customizationSettings().get(instance.getRequiredCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        MimicMiraculousActiveAbilityState mimicMiraculousActiveAbilityState = PerformerAbilityData.get(performer, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_ACTIVE);
        if (mimicMiraculousActiveAbilityState != null) {
            if (context.source().customizationSettings().get(instance.getOptionalCustomizationSetting(TIMER))) {}
            return AbilityUtils.performActiveAbility(Optional.of(miraculousHolder.value().activeAbility()), Ability.Context.of(context.level(), performer, createProxyData(mimicMiraculousActiveAbilityState.customizationData(), context), miraculousHolder, context.target()));
        }
        return Ability.Result.fail();
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        ImmutableSet.Builder<AbilityProperty<?>> builder = ImmutableSet.builder();
        builder.addAll(super.getOptionalProperties());
        builder.add(TIMER);
        return builder.build();
    }
}
