package dev.thomasglasser.mineraculouskamikotizations.world.item;

import dev.thomasglasser.mineraculouskamikotizations.client.particle.FloatingBubbleParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BubbleWandItem extends Item {
    public BubbleWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level instanceof ServerLevel) {
            ItemStack stack = player.getItemInHand(usedHand);
            stack.hurtAndBreak(1, player, usedHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 eyePosition = entity.getEyePosition().subtract(0, 0.125F, 0);
            Vec3 bubblePosition = entity.getLookAngle().normalize().scale(0.4).add(eyePosition);
            serverLevel.sendParticles(
                    new FloatingBubbleParticleOptions(
                            0.2f,
                            0.8f,
                            1f,
                            120,
                            false),
                    bubblePosition.x, bubblePosition.y, bubblePosition.z,
                    level.random.nextInt(1, 2),
                    0, 0, 0, 0);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 30;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 30);
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
