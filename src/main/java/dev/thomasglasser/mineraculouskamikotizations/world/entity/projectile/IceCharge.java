package dev.thomasglasser.mineraculouskamikotizations.world.entity.projectile;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class IceCharge extends AbstractHurtingProjectile implements ItemSupplier {
    public IceCharge(EntityType<? extends IceCharge> entityType, Level level) {
        super(entityType, level);
        accelerationPower = 0.0;
    }

    public IceCharge(EntityType<? extends IceCharge> entityType, @Nullable Entity owner, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level);
        setOwner(owner);
        accelerationPower = 0.0;
    }

    public IceCharge(@Nullable Entity owner, double x, double y, double z, Level level) {
        this(MineraculousKamikotizationsEntityTypes.ICE_CHARGE.get(), owner, x, y, z, level);
    }

    public IceCharge(double x, double y, double z, Vec3 movement, Level level) {
        super(MineraculousKamikotizationsEntityTypes.ICE_CHARGE.get(), x, y, z, movement, level);
        accelerationPower = 0.0;
    }

    @Override
    protected AABB makeBoundingBox() {
        float middle = getType().getDimensions().width() / 2.0F;
        float height = getType().getDimensions().height();
        return new AABB(
                position().x - (double) middle,
                position().y - 0.15F,
                position().z - (double) middle,
                position().x + (double) middle,
                position().y - 0.15F + (double) height,
                position().z + (double) middle);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof IceCharge) && super.canCollideWith(entity);
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return !(target instanceof IceCharge) && target.getType() != EntityType.END_CRYSTAL && super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level() instanceof ServerLevel level) {
            LivingEntity owner = getOwner() instanceof LivingEntity living ? living : null;
            Entity entity = result.getEntity();
            if (owner != null)
                owner.setLastHurtMob(entity);

            DamageSource damageSource = damageSources().windCharge(this, owner);
            if (entity.hurt(damageSource, 1.0F) && entity instanceof LivingEntity livingEntity)
                EnchantmentHelper.doPostAttackEffects(level, livingEntity, damageSource);

            entity.setTicksFrozen(Integer.MAX_VALUE);
            entity.extinguishFire();
            entity.moveTo(entity.blockPosition().getBottomCenter());
            entity.setDeltaMovement(0, 0, 0);
            applyToSurroundingBlocks(entity, level, (pos, state) -> level.setBlockAndUpdate(pos, Blocks.BLUE_ICE.defaultBlockState()));
        }
    }

    protected void applyToSurroundingBlocks(Entity entity, Level level, BiConsumer<BlockPos, BlockState> consumer) {
        ImmutableSet<BlockPos> inside = getInsidePos(entity);
        int blocksWide = Mth.ceil(entity.getBbWidth());
        int blocksHigh = Mth.ceil(entity.getBbHeight());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = -blocksWide; i <= blocksWide; i++) {
            for (int j = -1; j <= blocksHigh; j++) {
                for (int k = -blocksWide; k <= blocksWide; k++) {
                    pos.setWithOffset(entity.blockPosition(), i, j, k);
                    BlockState state = level.getBlockState(pos);
                    if (!inside.contains(pos) && state.canBeReplaced())
                        consumer.accept(pos, state);
                }
            }
        }
    }

    public static ImmutableSet<BlockPos> getInsidePos(Entity target) {
        AABB boundingBox = target.getBoundingBox();
        BlockPos startPos = BlockPos.containing(boundingBox.minX + 1.0E-7, boundingBox.minY + 1.0E-7, boundingBox.minZ + 1.0E-7);
        BlockPos endPos = BlockPos.containing(boundingBox.maxX - 1.0E-7, boundingBox.maxY - 1.0E-7, boundingBox.maxZ - 1.0E-7);
        ImmutableSet.Builder<BlockPos> inside = new ImmutableSet.Builder<>();
        for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos))
            inside.add(pos.immutable());
        return inside.build();
    }

    @Override
    public void push(double x, double y, double z) {}

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Level level = level();
        if (!level.isClientSide()) {
            applyToSurroundingBlocks(result.getBlockPos(), level, (pos, state) -> level.setBlockAndUpdate(pos, Blocks.BLUE_ICE.defaultBlockState()));
            discard();
        }
    }

    protected void applyToSurroundingBlocks(BlockPos center, Level level, BiConsumer<BlockPos, BlockState> consumer) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();
        for (int i = -1; i <= 1; i++)
            for (int k = -1; k <= 1; k++) {
                pos.setWithOffset(center, i, 0, k);
                abovePos.setWithOffset(pos, 0, 1, 0);
                if (level.canSeeSky(abovePos))
                    abovePos.set(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos));
                BlockState aboveState = level.getBlockState(abovePos);
                if (aboveState.canBeReplaced() && !level.getBlockState(pos).is(BlockTags.ICE))
                    consumer.accept(abovePos, aboveState);
            }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide)
            discard();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected float getLiquidInertia() {
        return getInertia();
    }

    @Nullable
    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SNOWFLAKE;
    }

    @Override
    public void tick() {
        Level level = level();
        if (!level.isClientSide && getBlockY() > level.getMaxBuildHeight() + 30) {
            discard();
            return;
        }
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public ItemStack getItem() {
        return Items.BLUE_ICE.getDefaultInstance();
    }
}
