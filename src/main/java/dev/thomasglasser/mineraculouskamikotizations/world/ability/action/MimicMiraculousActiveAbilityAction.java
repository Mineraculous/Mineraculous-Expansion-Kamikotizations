package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class MimicMiraculousActiveAbilityAction extends AbstractMimicMiraculousAbilityAction {
    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        ServerLevel level = context.level();
        Holder<Miraculous> miraculousHolder = context.source().customizationSettings().get(instance.getCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        MimicMiraculousAbilityState mimicAbilityState = PerformerAbilityData.get(performer, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS);
        if (mimicAbilityState != null) {
            return AbilityUtils.performActiveAbility(Optional.of(miraculousHolder.value().activeAbility()), Ability.Context.of(level, performer, mimicAbilityState.data(), miraculousHolder, context.target()));
        }
        return Ability.Result.fail();
    }
}
