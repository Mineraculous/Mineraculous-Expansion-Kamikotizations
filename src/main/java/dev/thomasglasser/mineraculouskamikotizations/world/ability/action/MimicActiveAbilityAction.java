package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MimicActiveAbilityAction implements AbilityAction {
    public static final CustomizationSettingAbilityProperty<Holder<Miraculous>> MIRACULOUS = MineraculousKamikotizationsAbilityProperties.MIRACULOUS;
    public static final CustomizationSettingAbilityProperty<UUID> PLAYER = MineraculousKamikotizationsAbilityProperties.PLAYER;
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(MIRACULOUS);
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(PLAYER);

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        ServerLevel level = context.level();
        CustomizationSettings settings = context.source().customizationSettings();
        UUID playerUUID = settings.get(instance.getCustomizationSetting(PLAYER));
        System.out.println(settings);
        System.out.println(playerUUID);
        if (playerUUID != null) {
            Player player = level.getPlayerByUUID(playerUUID);
            System.out.println(player.getName());
            if (player != null && !player.isRemoved() && player.hasData(MineraculousAttachmentTypes.MIRACULOUSES)) {
                MiraculousData miraculousData = player.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(settings.get(instance.getCustomizationSetting(MIRACULOUS)));
                System.out.println(context.performer());
                System.out.println(miraculousData);
                PerformerAbilityData.add(context.performer(), new MimicAbilityState(miraculousData));
            }
        }
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC.get());
    }

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        ServerLevel level = context.level();
        CustomizationSettings settings = context.source().customizationSettings();
        Holder<Miraculous> miraculousHolder = settings.get(instance.getCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        if (PerformerAbilityData.has(performer, MineraculousKamikotizationsAbilityStates.MIMIC)) {
            MimicAbilityState mimicAbilityState = PerformerAbilityData.get(performer, MineraculousKamikotizationsAbilityStates.MIMIC);
            System.out.println(mimicAbilityState);
            System.out.println(miraculousHolder);
            if (mimicAbilityState != null && mimicAbilityState.data() != null) {
                System.out.println(2);
                miraculousHolder.value().activeAbility().perform(Ability.Context.of(level, performer, mimicAbilityState.data(), miraculousHolder, context.target()));
                return Ability.Result.succeedAndStop();
            }
        }
        return Ability.Result.pass();
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
