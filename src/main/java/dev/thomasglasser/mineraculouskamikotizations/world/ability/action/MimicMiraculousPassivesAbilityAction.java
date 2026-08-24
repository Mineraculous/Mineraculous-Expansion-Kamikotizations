package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousPassivesAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

public class MimicMiraculousPassivesAbilityAction extends AbstractMimicMiraculousAbilityAction {
    @Override
    protected AbilityState createState(CustomizationData customizationData) {
        return new MimicMiraculousPassivesAbilityState(customizationData);
    }

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        Holder<Miraculous> miraculousHolder = context.source().customizationSettings().get(instance.getRequiredCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        MimicMiraculousPassivesAbilityState mimicMiraculousPassivesAbilityState = PerformerAbilityData.get(performer, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_PASSIVES);
        if (mimicMiraculousPassivesAbilityState != null) {
            MiraculousData proxyData = createProxyData(mimicMiraculousPassivesAbilityState.customizationData(), context);
            return AbilityUtils.performPassiveAbilities(miraculousHolder.value().passiveAbilities(), Ability.Context.of(context.level(), performer, proxyData, miraculousHolder, context.target()));
        }
        return Ability.Result.fail();
    }
}
