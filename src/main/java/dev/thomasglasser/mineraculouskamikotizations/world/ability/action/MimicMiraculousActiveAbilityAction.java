package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousActiveAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MimicMiraculousActiveAbilityAction extends AbstractMimicMiraculousAbilityAction {
    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        ServerLevel level = context.level();
        Holder<Miraculous> miraculousHolder = context.source().customizationSettings().get(instance.getCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        MimicMiraculousActiveAbilityState mimicActiveAbilityState = PerformerAbilityData.get(performer, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_ACTIVE);
        if (mimicActiveAbilityState != null) {
            return AbilityUtils.performActiveAbility(Optional.of(miraculousHolder.value().activeAbility()), Ability.Context.of(level, performer, mimicActiveAbilityState.data(), miraculousHolder, context.target()));
        }
        return Ability.Result.fail();
    }

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        CustomizationSettings settings = context.source().customizationSettings();
        UUID targetUUID = settings.get(instance.getCustomizationSetting(TARGET));
        Holder<Miraculous> miraculousHolder = settings.get(instance.getCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        if (targetUUID != null && context.level().getPlayerByUUID(targetUUID) instanceof Player player && !player.isRemoved()) {
            PerformerAbilityData.add(performer, new MimicMiraculousActiveAbilityState(player.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(miraculousHolder)));
        } else {
            PerformerAbilityData.add(performer, new MimicMiraculousActiveAbilityState(performer.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(miraculousHolder)));
        }
    }
}
