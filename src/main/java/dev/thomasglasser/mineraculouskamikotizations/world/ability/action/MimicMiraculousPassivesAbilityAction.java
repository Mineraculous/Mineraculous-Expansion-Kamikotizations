package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

public class MimicMiraculousPassivesAbilityAction extends AbstractMimicMiraculousAbilityAction {
    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        Holder<Miraculous> miraculousHolder = context.source().customizationSettings().get(instance.getCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        MimicMiraculousAbilityState mimicAbilityState = PerformerAbilityData.get(performer, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS);
        if (mimicAbilityState != null) {
            Miraculous miraculous = miraculousHolder.value();
            MiraculousData miraculousData = mimicAbilityState.data();
            return AbilityUtils.performPassiveAbilities(miraculous.passiveAbilities(), context.level(), performer, miraculousHolder.getKey(), miraculous.customizationSettings(), miraculousData.powerLevel(), miraculous.color().getValue(), miraculousData.powerActive(), miraculousData.storedEntities(), AbilityUtils.findItem(performer, stack -> miraculousData.isTool(stack, performer, miraculousHolder)));
        }
        return Ability.Result.fail();
    }
}
