package dev.thomasglasser.mineraculouskamikotizations.world.item;

import dev.thomasglasser.mineraculous.impl.world.item.component.Active;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

public class ParasolItem extends Item {
    public static final ResourceLocation BASE_FALL_DAMAGE_MULTIPLIER_ID = ResourceLocation.withDefaultNamespace("base_fall_damage_multiplier");

    private static final ItemAttributeModifiers ACTIVE_MODIFIERS = ItemAttributeModifiers.builder()
            .add(Attributes.FALL_DAMAGE_MULTIPLIER, new AttributeModifier(BASE_FALL_DAMAGE_MULTIPLIER_ID, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ANY)
            .build();

    public ParasolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (Active.isActive(player.getItemInHand(usedHand))) {
            player.startUsingItem(usedHand);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING));
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return SharedConstants.TICKS_PER_MINUTE;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        if (Active.isActive(stack))
            return ACTIVE_MODIFIERS;
        return super.getDefaultAttributeModifiers(stack);
    }
}
