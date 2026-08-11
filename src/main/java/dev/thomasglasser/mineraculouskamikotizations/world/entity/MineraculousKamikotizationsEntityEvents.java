package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.BubbleImprisonedData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.MineraculousKamikotizationsAttachementTypes;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
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

        Optional<BubbleImprisonedData> bubbleData = entity.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
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

    public static void onEntityIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA).isPresent()) {
            Entity bubble = victim.level().getEntity(victim.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA).get().bubbleID());
            if (bubble instanceof BubblePrison bubblePrison) {
                if (event.getSource().getEntity() != bubblePrison) {
                    bubblePrison.hurt(event.getSource(), event.getAmount());
                    event.setCanceled(true);
                }
            }
        }
    }

    public static void onProjectileImpact(ProjectileImpactEvent event) {
        HitResult hit = event.getRayTraceResult();

        if (hit.getType() == HitResult.Type.ENTITY && hit instanceof EntityHitResult entityHit) {
            entityHit.getEntity().getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA).ifPresent(data -> {
                Entity bubble = entityHit.getEntity().level().getEntity(data.bubbleID());

                if (bubble instanceof BubblePrison bubblePrison) {
                    bubblePrison.hurt(bubble.damageSources().thrown(event.getProjectile(), event.getProjectile().getOwner()), 0.0f);
                    event.setCanceled(true);
                    event.getProjectile().discard();
                }
            });
        }
    }
}
