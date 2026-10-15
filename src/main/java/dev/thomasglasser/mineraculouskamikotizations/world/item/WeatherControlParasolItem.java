package dev.thomasglasser.mineraculouskamikotizations.world.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.client.gui.selection.HotbarMenuProvider;
import dev.thomasglasser.mineraculous.api.core.component.MineraculousDataComponents;
import dev.thomasglasser.mineraculous.api.world.item.KamikotizationBoundItem;
import dev.thomasglasser.mineraculous.api.world.item.MineraculousItemUtils;
import dev.thomasglasser.mineraculous.api.world.kamikotization.Kamikotization;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.ReversionData;
import dev.thomasglasser.mineraculous.impl.world.item.component.Active;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.selection.MineraculousKamikotizationsHotbarMenuIcons;
import dev.thomasglasser.mineraculouskamikotizations.core.component.MineraculousKamikotizationsDataComponents;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization.MineraculousKamikotizationsKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting.RevertingIceCharge;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting.RevertingLightningBolt;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting.RevertingWindCharge;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.reversion.entry.RestoreWeatherReversionEntry;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WeatherControlParasolItem extends ParasolItem implements GeoItem, KamikotizationBoundItem, HotbarMenuProvider<WeatherControlParasolItem.Mode> {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WeatherControlParasolItem(Properties properties) {
        super(properties);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public ResourceKey<Kamikotization> getKamikotization() {
        return MineraculousKamikotizationsKamikotizations.WEATHER_CONTROL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (Active.isActive(stack))
            return super.use(level, player, usedHand);
        Mode mode = stack.get(MineraculousKamikotizationsDataComponents.WEATHER_CONTROL_PARASOL_MODE);
        if (mode == null) return InteractionResultHolder.fail(stack);
        if (level instanceof ServerLevel serverLevel) {
            UUID ownerId = stack.get(MineraculousDataComponents.OWNER);
            Entity owner = ownerId != null ? serverLevel.getEntity(ownerId) : null;
            switch (mode) {
                case ICE -> {
                    RevertingIceCharge iceCharge = new RevertingIceCharge(owner, player.getX(), player.getEyeY(), player.getZ(), level);
                    iceCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                    level.addFreshEntity(iceCharge);
                    level.playSound(null, player.getX(), player.getEyeY(), player.getZ(), SoundEvents.WIND_CHARGE_THROW, iceCharge.getSoundSource(), 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                }
                case LIGHTNING -> {
                    HitResult hitresult = ProjectileUtil.getHitResultOnViewVector(player, entity -> entity.isAlive() && entity.isPickable(), 100);
                    RevertingLightningBolt lightningBolt = MineraculousKamikotizationsEntityTypes.REVERTING_LIGHTNING_BOLT.get().create(level);
                    if (lightningBolt != null) {
                        if (owner instanceof ServerPlayer serverPlayer)
                            lightningBolt.setCause(serverPlayer);
                        if (hitresult instanceof EntityHitResult entityHitResult)
                            lightningBolt.setPos(entityHitResult.getEntity().position());
                        else
                            lightningBolt.setPos(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(hitresult.getLocation())).getBottomCenter());
                        level.addFreshEntity(lightningBolt);
                    }
                }
                case WEATHER -> {
                    if (owner != null)
                        ReversionData.get(serverLevel).putEntry(RestoreWeatherReversionEntry.of(owner.getUUID(), serverLevel));
                    RandomSource random = serverLevel.random;
                    if (serverLevel.isThundering())
                        serverLevel.setWeatherParameters(ServerLevel.RAIN_DELAY.sample(random), 0, false, false);
                    else if (serverLevel.isRaining())
                        serverLevel.setWeatherParameters(0, ServerLevel.THUNDER_DURATION.sample(random), true, true);
                    else
                        serverLevel.setWeatherParameters(0, ServerLevel.RAIN_DURATION.sample(random), true, false);
                }
                case WIND -> {
                    RevertingWindCharge windCharge = new RevertingWindCharge(owner, level, player.getX(), player.getEyeY(), player.getZ());
                    windCharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                    level.addFreshEntity(windCharge);
                    level.playSound(null, player.getX(), player.getEyeY(), player.getZ(), SoundEvents.WIND_CHARGE_THROW, windCharge.getSoundSource(), 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                }
            }
        }
        player.getCooldowns().addCooldown(this, 10);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public ImmutableList<Mode> getOptions(ItemStack stack, InteractionHand hand, Player holder) {
        return Mode.valuesList();
    }

    @Override
    public boolean canOpenHotbarMenu(ItemStack stack, Player holder) {
        return !Active.isActive(stack);
    }

    @Override
    public Supplier<DataComponentType<Mode>> getOptionComponentType() {
        return MineraculousKamikotizationsDataComponents.WEATHER_CONTROL_PARASOL_MODE;
    }

    public enum Mode implements HotbarMenuProvider.HotbarMenuOption {
        ICE(MineraculousKamikotizationsHotbarMenuIcons.ICE),
        LIGHTNING(MineraculousKamikotizationsHotbarMenuIcons.LIGHTNING),
        WEATHER(MineraculousKamikotizationsHotbarMenuIcons.CLOUD),
        WIND(MineraculousKamikotizationsHotbarMenuIcons.WIND);

        public static final Codec<Mode> CODEC = StringRepresentable.fromEnum(Mode::values);
        public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(Mode::of, Mode::getSerializedName);

        private static final ImmutableList<Mode> VALUES_LIST = ImmutableList.copyOf(values());

        private final Supplier<Component> displayName;
        private final ResourceLocation icon;

        Mode(ResourceLocation icon) {
            displayName = Suppliers.memoize(() -> MineraculousItemUtils.translatableItemComponent(MineraculousKamikotizationsDataComponents.WEATHER_CONTROL_PARASOL_MODE, getSerializedName()));
            this.icon = icon;
        }

        public static ImmutableList<Mode> valuesList() {
            return VALUES_LIST;
        }

        public static Mode of(String name) {
            return valueOf(name.toUpperCase());
        }

        @Override
        public Component displayName() {
            return displayName.get();
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        @Override
        public ResourceLocation icon() {
            return icon;
        }
    }
}
