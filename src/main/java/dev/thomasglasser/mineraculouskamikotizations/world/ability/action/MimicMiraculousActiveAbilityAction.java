package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityUtils;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MimicMiraculousActiveAbilityAction implements AbilityAction {
    public static final CustomizationSettingAbilityProperty<Holder<Miraculous>> MIRACULOUS = MineraculousKamikotizationsAbilityProperties.MIRACULOUS;
    public static final CustomizationSettingAbilityProperty<UUID> TARGET = MineraculousKamikotizationsAbilityProperties.TARGET;
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(MIRACULOUS);
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(TARGET);

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        ServerLevel level = context.level();
        Holder<Miraculous> miraculousHolder = context.source().customizationSettings().get(instance.getCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        MimicMiraculousAbilityState mimicAbilityState = PerformerAbilityData.get(performer, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS);
        if (mimicAbilityState != null) {
            Miraculous miraculous = miraculousHolder.value();
            MiraculousData miraculousData = mimicAbilityState.data();
            return AbilityUtils.performPassiveAbilities(miraculous.passiveAbilities(), level, performer, miraculousHolder.getKey(), miraculous.customizationSettings(), miraculousData.powerLevel(), miraculous.color().getValue(), miraculousData.powerActive(), miraculousData.storedEntities(), AbilityUtils.findItem(performer, stack -> miraculousData.isTool(stack, performer, miraculousHolder)));
        }
        return Ability.Result.fail();
    }

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        CustomizationSettings settings = context.source().customizationSettings();
        UUID targetUUID = settings.get(instance.getCustomizationSetting(TARGET));
        Holder<Miraculous> miraculousHolder = settings.get(instance.getCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        if (targetUUID != null) {
            // TODO: get data from offline players
            Player player = context.level().getPlayerByUUID(targetUUID);
            if (player != null && !player.isRemoved()) {
                PerformerAbilityData.add(performer, new MimicMiraculousAbilityState(player.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(miraculousHolder)));
            }
        } else {
            PerformerAbilityData.add(performer, new MimicMiraculousAbilityState(performer.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(miraculousHolder)));
        }
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS.get());
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getRequiredProperties() {
        return REQUIRED_PROPERTIES;
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        return OPTIONAL_PROPERTIES;
    }
}
