package dev.thomasglasser.mineraculouskamikotizations.world.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.client.gui.screens.RadialMenuOption;
import dev.thomasglasser.mineraculous.api.core.component.MineraculousDataComponents;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.item.MineraculousItems;
import dev.thomasglasser.mineraculous.api.world.item.RadialMenuProvider;
import dev.thomasglasser.mineraculous.api.world.item.armor.MineraculousArmors;
import dev.thomasglasser.mineraculous.api.world.kamikotization.Kamikotization;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculouses;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.client.particle.FloatingBubbleParticleOptions;
import dev.thomasglasser.mineraculouskamikotizations.core.component.MineraculousKamikotizationsDataComponents;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.BubblePrison;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization.MineraculousKamikotizationsKamikotizations;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BubbleSwordItem extends Item implements ICurioItem, RadialMenuProvider<BubbleSwordItem.Mode> {
    public static final AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(MineraculousKamikotizations.modLoc("sword_flight"), 1, AttributeModifier.Operation.ADD_VALUE);

    private static Multimap<Holder<Attribute>, AttributeModifier> CURIOS_ATTRIBUTE_MODIFIERS;

    public BubbleSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        ItemStack chestplate = slotContext.entity().getItemBySlot(EquipmentSlot.CHEST);
        Holder<Kamikotization> kamikotization = chestplate.get(MineraculousDataComponents.KAMIKOTIZATION);
        // TODO: Switch to tag
        return chestplate.is(MineraculousArmors.KAMIKOTIZATION.chest()) && kamikotization != null && kamikotization.is(MineraculousKamikotizationsKamikotizations.BUBBLE_CAPTURE);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        if (CURIOS_ATTRIBUTE_MODIFIERS == null) {
            CURIOS_ATTRIBUTE_MODIFIERS = HashMultimap.create();
            CURIOS_ATTRIBUTE_MODIFIERS.put(NeoForgeMod.CREATIVE_FLIGHT, FLIGHT_MODIFIER);
        }
        return CURIOS_ATTRIBUTE_MODIFIERS;
    }

    @Override
    public int getColor(ItemStack stack, InteractionHand hand, Player holder) {
        Level level = holder.level();
        int color = level.holderOrThrow(Miraculouses.BUTTERFLY).value().color().getValue();
        ResolvableProfile resolvableProfile = stack.get(DataComponents.PROFILE);
        if (resolvableProfile != null) {
            Player owner = level.getPlayerByUUID(resolvableProfile.id().orElse(resolvableProfile.gameProfile().getId()));
            if (owner != null && owner.getData(MineraculousAttachmentTypes.KAMIKOTIZATION).isPresent())
                color = owner.getData(MineraculousAttachmentTypes.KAMIKOTIZATION).get().kamikoData().nameColor();
        }
        return color;
    }

    @Override
    public List<Mode> getOptions(ItemStack stack, InteractionHand hand, Player holder) {
        return Mode.valuesList();
    }

    @Override
    public Supplier<DataComponentType<Mode>> getComponentType(ItemStack stack, InteractionHand hand, Player holder) {
        return MineraculousKamikotizationsDataComponents.BUBBLE_SWORD_MODE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 100;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getMainHandItem();
            Mode mode = stack.get(MineraculousKamikotizationsDataComponents.BUBBLE_SWORD_MODE);
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(stack);
            }
            if (mode == Mode.EXPLOSIVE) {
                if (!level.isClientSide()) {
                    BubblePrison bubblePrison = new BubblePrison(player, level, BubblePrison.BubbleBehavior.EXPLODING, 0.4);
                    bubblePrison.setDeltaMovement(player.getLookAngle().normalize().scale(4));
                    bubblePrison.setPos(player.position().add(0, player.getEyeHeight(), 0).add(player.getLookAngle().normalize().scale(4)));
                    level.addFreshEntity(bubblePrison);
                    player.getCooldowns().addCooldown(this, 20);
                }
                return InteractionResultHolder.success(stack);
            } else if (mode == Mode.CAPTURE) {
                if (!level.isClientSide()) {
                    BubblePrison bubblePrison = new BubblePrison(player, level, BubblePrison.BubbleBehavior.TARGETING, 1);
                    bubblePrison.setDeltaMovement(player.getLookAngle().normalize());
                    bubblePrison.setPos(player.position().add(0, player.getEyeHeight(), 0).add(player.getLookAngle().normalize().scale(4)));
                    level.addFreshEntity(bubblePrison);
                    player.getCooldowns().addCooldown(this, 55);
                }
                return InteractionResultHolder.success(stack);
            } else if (mode == Mode.FLIGHT) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof Player player) {
            Mode mode = stack.get(MineraculousKamikotizationsDataComponents.BUBBLE_SWORD_MODE);
            if (mode == Mode.FLIGHT) {
                player.setDeltaMovement(player.getLookAngle().normalize().scale(1.5));
                player.resetFallDistance();
                FloatingBubbleParticleOptions.spawnGreen(level, player.position(), 10, true);
                int timeHeld = this.getUseDuration(stack, entity) - remainingUseDuration;
                if (timeHeld % 10 == 0 && !level.isClientSide) {
                    BubblePrison bubblePrison = new BubblePrison(player, level, BubblePrison.BubbleBehavior.TARGETING, 0.1);
                    bubblePrison.setDeltaMovement(player.getDeltaMovement().scale(-0.2));
                    bubblePrison.moveTo(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
                    level.addFreshEntity(bubblePrison);
                    bubblePrison.refreshDimensions();
                }
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            Mode mode = stack.get(MineraculousKamikotizationsDataComponents.BUBBLE_SWORD_MODE);
            if (mode == Mode.FLIGHT) {
                player.getCooldowns().addCooldown(this, 35);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 30);
        }
    }

    public enum Mode implements RadialMenuOption, StringRepresentable {
        EXPLOSIVE,
        FLIGHT,
        CAPTURE;

        public static final Codec<Mode> CODEC = StringRepresentable.fromEnum(Mode::values);
        public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(Mode::of, Mode::getSerializedName);

        private static final List<Mode> VALUES_LIST = new ReferenceArrayList<>(values());

        private final Component displayName;

        Mode() {
            this.displayName = Component.translatable(MineraculousItems.BUTTERFLY_CANE.getId().toLanguageKey("mode", getSerializedName()));
        }

        @Override
        public Component displayName() {
            return displayName;
        }

        @Override
        public boolean isEnabled(ItemStack stack, Player holder) {
            return true;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        public static List<Mode> valuesList() {
            return VALUES_LIST;
        }

        public static Mode of(String name) {
            return valueOf(name.toUpperCase());
        }
    }
}
