package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.HolderAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class ReplicateActiveAbilityAction implements AbilityAction {
    public static final HolderAbilityProperty<Miraculous> MIRACULOUS = MineraculousKamikotizationsAbilityProperties.MIRACULOUS;
    public static final AbilityProperty<UUID> PLAYER = MineraculousKamikotizationsAbilityProperties.PLAYER;
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of();
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(MIRACULOUS, PLAYER);

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        ServerLevel level = context.level();
        // TODO: Tommy fix the holder property screen not working so it doesn't return null
        Holder<Miraculous> miraculousHolder = instance.getOptionalProperty(MIRACULOUS);
        UUID playerUUID = instance.getOptionalProperty(PLAYER);
        if (playerUUID != null) {
            Player player = level.getPlayerByUUID(playerUUID);
            if (miraculousHolder != null && player != null) {
                miraculousHolder.value().activeAbility().perform(Ability.Context.of(level, context.performer(), player.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(miraculousHolder), miraculousHolder, context.target()));
                return Ability.Result.succeedAndContinue();
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
