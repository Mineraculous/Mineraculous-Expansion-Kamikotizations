package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameColorAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class MineraculousKamikotizationsEntityEvents {
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        Player player = event.getEntity();
        // TODO: tommy explain why this doesnt work
        MimicMiraculousNameColorAbilityState nameColorAbilityState = PerformerAbilityData.get(player, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME_COLOR);
        if (nameColorAbilityState != null) {
            event.setDisplayname(Entity.removeAction(event.getDisplayname().copy().setStyle(event.getDisplayname().getStyle().withColor(nameColorAbilityState.nameColor()).withHoverEvent(null))));
        }
    }
}
