package dev.thomasglasser.mineraculouskamikotizations.world.item;

import dev.thomasglasser.mineraculous.api.core.component.MineraculousDataComponents;
import dev.thomasglasser.mineraculous.impl.world.item.component.Active;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ParasolItem extends Item {
    public static final ResourceLocation BASE_FALL_DAMAGE_MULTIPLIER_ID = ResourceLocation.withDefaultNamespace("base_fall_damage_multiplier");

    private static final ItemAttributeModifiers ACTIVE_MODIFIERS = ItemAttributeModifiers.builder()
            .add(Attributes.FALL_DAMAGE_MULTIPLIER, new AttributeModifier(BASE_FALL_DAMAGE_MULTIPLIER_ID, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.ANY)
            .build();

    public ParasolItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        if (stack.getOrDefault(MineraculousDataComponents.ACTIVE, Active.DEFAULT).active())
            return ACTIVE_MODIFIERS;
        return super.getDefaultAttributeModifiers(stack);
    }
}
