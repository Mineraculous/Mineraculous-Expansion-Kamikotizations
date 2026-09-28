package dev.thomasglasser.mineraculouskamikotizations.event;

import dev.thomasglasser.mineraculous.api.event.MiraculousEvent;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import net.minecraft.server.level.ServerLevel;

public class MineraculousKamikotizationsMiraculousEvents {
    public static void onFinishMiraculousTransform(MiraculousEvent.Transform.Finish event) {
        if (event.getEntity().level() instanceof ServerLevel level)
            TransformationHistoryData.get(level).recordTransformation(event.getMiraculous(), event.getMiraculousData().customizationData());
    }
}
