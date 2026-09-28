package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class MineraculousKamikotizationsEntityEvents {
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        event.setDisplayname(MineraculousKamikotizationEntityUtils.formatDisplayName(event.getEntity(), event.getDisplayname()));
    }
}
