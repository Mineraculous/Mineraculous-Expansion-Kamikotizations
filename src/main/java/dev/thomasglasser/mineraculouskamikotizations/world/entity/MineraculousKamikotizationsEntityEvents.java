package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.BubblePrisonData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.MineraculousKamikotizationsAttachementTypes;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class MineraculousKamikotizationsEntityEvents {
    public static void onEntityMount(EntityMountEvent event) {
        Entity mounting = event.getEntityMounting();
        Entity mounted = event.getEntityBeingMounted();
        if (event.isDismounting() && !(mounting.isRemoved() || mounting.isSpectator() || mounting instanceof Player player && player.isCreative()) && !mounted.isRemoved() && mounted instanceof BubblePrison) {
            event.setCanceled(true);
        }
    }

    public static void onEntityPostTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        Level level = entity.level();

        Optional<BubblePrisonData> bubbleData = entity.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
        if (bubbleData.isPresent()) {
            BubblePrison.onCaptiveEntityTick(level, entity, bubbleData);
        }
    }

    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        Entity entity = event.getEntity();
        Level level = event.getLevel();
        if (entity instanceof BubblePrison bubblePrison) {
            bubblePrison.entityLeaveLevel(level);
        }
    }
}
