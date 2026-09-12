package dev.thomasglasser.mineraculouskamikotizations.event;

import dev.thomasglasser.mineraculous.api.event.MiraculousEvent;
import dev.thomasglasser.mineraculous.impl.event.MiraculousEvents;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class MineraculousKamikotizationsMiraculousEvents {
    public static void onFinishMiraculousTransform(MiraculousEvent.Transform.Finish event) {
        LivingEntity entity = event.getEntity();
        if (MiraculousEvents.isPowered(entity) && entity.level() instanceof ServerLevel level) {
            TransformationHistoryData.get(level).recordTransformation(event.getMiraculous(), event.getMiraculousData().customizationData());
        }
    }
}
