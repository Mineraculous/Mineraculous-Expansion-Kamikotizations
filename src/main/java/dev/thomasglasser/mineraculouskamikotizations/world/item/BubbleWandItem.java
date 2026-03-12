package dev.thomasglasser.mineraculouskamikotizations.world.item;

import dev.thomasglasser.mineraculouskamikotizations.core.particles.MineraculousKamikotizationsParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BubbleWandItem extends Item {
    public BubbleWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 eyePosition = player.getEyePosition().subtract(0, 0.125F, 0);
            Vec3 bubblePosition = player.getLookAngle().normalize().scale(0.4).add(eyePosition);
            serverLevel.sendParticles(MineraculousKamikotizationsParticleTypes.FLOATING_BUBBLE.get(), bubblePosition.x, bubblePosition.y, bubblePosition.z, level.random.nextInt(1, 8), 0, 0, 0, 0);
            player.getItemInHand(usedHand).hurtAndBreak(1, player, usedHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        return super.use(level, player, usedHand);
    }
}
