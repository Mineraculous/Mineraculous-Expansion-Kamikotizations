package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculous.api.event.CollectMiraculousLadybugTargetsEvent;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.ReversionData;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.ReversionEntry;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.reversion.entry.RestoreWeatherReversionEntry;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;

public class MineraculousKamikotizationsEntityEvents {
    // TODO: Shouldn't be required
    public static void onCollectMiraculousLadybugTargets(CollectMiraculousLadybugTargetsEvent event) {
        ServerLevel level = event.getLevel();
        ReversionData reversionData = ReversionData.get(level);
        for (UUID cause : reversionData.getAllCausesFor(event.getTargetId()))
            for (ReversionEntry<?, ?> entry : reversionData.getEntriesForCause(cause))
                if (entry instanceof RestoreWeatherReversionEntry)
                    reversionData.tryRevert(entry, level);
    }
}
