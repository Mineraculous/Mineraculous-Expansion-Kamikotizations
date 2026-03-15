package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.world.level.storage.BlockReversionData;
import dev.thomasglasser.mineraculous.api.world.level.storage.EntityReversionData;
import dev.thomasglasser.mineraculouskamikotizations.client.particle.FloatingBubbleParticleOptions;
import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsDamageTypeTags;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.BubbleImprisonedData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.BubblePrisonData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.MineraculousKamikotizationsAttachementTypes;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BubblePrison extends Entity implements OwnableEntity {
    private int targetingToCapturingTickCooldown = 10;
    private LivingEntity target;
    private int retargetCooldown = 0;
    private int bounceCount = 0;
    private UUID ownerUUID;

    private static final int MAX_BOUNCES = 8;
    private static final EntityDataAccessor<BubblePrisonData> DATA_BUBBLE = SynchedEntityData.defineId(BubblePrison.class, MineraculousKamikotizationsEntityDataSerializers.BUBBLE_PRISON_DATA.get());

    public BubblePrison(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
        this.noPhysics = false;
    }

    public BubblePrison(LivingEntity owner, Level level, BubbleBehavior behavior, double scaleFactor) {
        super(MineraculousKamikotizationsEntityTypes.BUBBLE_PRISON.get(), level);
        this.blocksBuilding = true;
        this.noPhysics = false;
        this.setOwner(owner);
        this.setBehavior(behavior);
        this.setScaleFactor(scaleFactor);
        refreshDimensions();
        if (level instanceof ServerLevel serverLevel) {
            EntityReversionData.get(serverLevel).putRemovable(owner.getUUID(), this);
        }
    }

    private BubblePrisonData getBubbleData() {
        return entityData.get(DATA_BUBBLE);
    }

    public BubbleBehavior getBehavior() {
        return getBubbleData().behavior();
    }

    public void setBehavior(BubbleBehavior behavior) {
        entityData.set(DATA_BUBBLE, getBubbleData().withBehavior(behavior));
        if (behavior == BubbleBehavior.TARGETING) {
            targetingToCapturingTickCooldown = 20;
        }
    }

    public List<UUID> getCaptives() {
        return new ArrayList<>(getBubbleData().captiveEntities());
    }

    public void setCaptives(List<UUID> list) {
        entityData.set(DATA_BUBBLE, getBubbleData().withCaptiveEntities(list));
    }

    public double getScaleFactor() {
        return getBubbleData().scaleFactor();
    }

    public void setScaleFactor(double scaleFactor) {
        entityData.set(DATA_BUBBLE, getBubbleData().withScaleFactor(scaleFactor));
        refreshDimensions();
    }

    @Override
    public void tick() {
        if (getBehavior() == BubbleBehavior.TARGETING) {
            tickTargeting();
        } else if (getBehavior() == BubbleBehavior.CAPTURING) {
            tickCapturing();
        } else if (getBehavior() == BubbleBehavior.EXPLODING) {
            tickExploding();
        }
        if (getScaleFactor() != 1 && getBbHeight() == 3.7f)
            refreshDimensions();
        this.move(MoverType.SELF, getDeltaMovement());
        super.tick();
    }

    private void tickExploding() {
        setDeltaMovement(getDeltaMovement().scale(0.93).add(0, -0.05, 0));
        hurtMarked = true;
        if (this.horizontalCollision || this.verticalCollision) {
            explodeBubble();
            return;
        }
        List<Entity> entities = level().getEntities(this, getBoundingBox().inflate(0.2));
        for (Entity entity : entities) {
            if (entity != this && entity.isAlive() && !entity.getUUID().equals(getOwnerUUID())) {
                explodeBubble();
                if (level() instanceof ServerLevel serverLevel && getOwnerUUID() != null) {
                    EntityReversionData.get(serverLevel).putRevertible(getOwnerUUID(), entity);
                }
                return;
            }
        }
    }

    private void explodeBubble() {
        if (level() instanceof ServerLevel serverLevel) {
            Explosion explosion = new Explosion(
                    level(),
                    this,
                    getX(),
                    getY() + getBbHeight() / 2,
                    getZ(),
                    2,
                    false,
                    Explosion.BlockInteraction.KEEP);

            explosion.explode();

            for (BlockPos pos : explosion.getToBlow()) {
                BlockState state = level().getBlockState(pos);
                if (!state.isAir() && getOwnerUUID() != null) {
                    BlockReversionData.get(serverLevel).putRevertible(getOwnerUUID(), level().dimension(), pos, state);
                    level().removeBlock(pos, false);
                }
            }

            level().playSound(null, getX(), getY(), getZ(),
                    SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS,
                    4.0F,
                    (1.0F + (level().random.nextFloat() - level().random.nextFloat()) * 0.2F) * 0.7F);

            explosion.finalizeExplosion(true);

            for (int i = 1; i <= 50; i++) {
                serverLevel.sendParticles(
                        new FloatingBubbleParticleOptions(
                                0.928f,
                                0.525f,
                                0.067f,
                                60,
                                true),
                        getX() + Math.random(), getY() + Math.random(), getZ() + Math.random(),
                        serverLevel.random.nextInt(1, 2),
                        0, 0, 0, 0);
            }
            discard();
        }
    }

    public double getPhysicalRadius() {
        return this.getBbHeight() * 0.324;
    }

    public void constrainCaptiveEntityPosition(LivingEntity captive) {
        double radius = getPhysicalRadius();
        Vec3 center = position().add(0, radius, 0);
        Vec3 toCaptured = captive.position().subtract(center);
        if (toCaptured.length() > radius) {
            captive.resetFallDistance();
            Vec3 correction = toCaptured.normalize().scale(radius).subtract(toCaptured);
            captive.move(MoverType.SELF, correction);
            captive.setDeltaMovement(captive.getDeltaMovement().multiply(1, 0.8, 1));
        }
    }

    private void tickCapturing() {
        pushNotCaptiveEntities();
        pushBubbleOnPlayerMovement();
    }

    public static void onCaptiveEntityTick(Level level, Entity entity, Optional<BubbleImprisonedData> bubbleData) {
        if (level instanceof ServerLevel serverLevel) {
            Entity bubble = serverLevel.getEntity(bubbleData.get().bubbleUUID());
            if (bubble == null || !bubble.isAlive() || bubble.isRemoved()) {
                entity.removeData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
            } else {
                BubbleImprisonedData updatedData = new BubbleImprisonedData(bubble.getUUID(), bubble.getId());
                if (!updatedData.equals(bubbleData.get()))
                    entity.setData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA, Optional.of(updatedData));
            }

        }
        Entity bubble = level.getEntity(bubbleData.get().bubbleID());
        if (entity instanceof LivingEntity livingEntity && bubble instanceof BubblePrison bubblePrison) {
            bubblePrison.constrainCaptiveEntityPosition(livingEntity);
        }
    }

    private void pushNotCaptiveEntities() {
        double radius = getPhysicalRadius();
        Vec3 center = position().add(0, radius, 0);
        List<Entity> entities = level().getEntities(this, getBoundingBox());
        for (Entity entity : entities) {
            Optional<BubbleImprisonedData> bubbleData = entity.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
            if (bubbleData.isEmpty() || !bubbleData.get().bubbleUUID().equals(getUUID())) {
                Vec3 toEntity = entity.position().subtract(center);
                double distance = toEntity.length();
                if (distance < 2.2 && !entity.noPhysics) {
                    Vec3 desiredPos = center.add(toEntity.normalize().scale(2.2));
                    Vec3 correction = desiredPos.subtract(entity.position());
                    entity.move(MoverType.SELF, correction);
                    entity.hurtMarked = true;
                }
            }
        }
    }

    private void pushBubbleOnPlayerMovement() {
        setDeltaMovement(0, 0, 0); //TODO apply vertical motion here
        if (level() instanceof ServerLevel serverLevel) {
            double radius = getPhysicalRadius();
            Vec3 center = position().add(0, radius, 0);
            Vec3 totalPush = Vec3.ZERO;
            int pushingPlayers = 0;
            double pushThreshold = radius * 0.5;
            for (UUID id : getCaptives()) {
                Entity entity = serverLevel.getEntity(id);
                if (entity instanceof ServerPlayer player) {
                    Vec3 offset = player.position().subtract(center);
                    offset = new Vec3(offset.x, 0, offset.z);
                    double distance = offset.length();
                    if (distance > pushThreshold) {
                        double strength = (distance - pushThreshold) / (radius - pushThreshold);
                        Vec3 push = offset.normalize().scale(strength);
                        totalPush = totalPush.add(push);
                        pushingPlayers++;
                    }
                }
            }
            if (pushingPlayers > 0) {
                Vec3 movement = totalPush.scale(1.0 / pushingPlayers);
                setDeltaMovement(movement);
                hurtMarked = true;
            }
        }
    }

    public void entityLeaveLevel(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            for (UUID id : getCaptives()) {
                Entity captive = serverLevel.getEntity(id);
                if (captive != null) {
                    captive.removeData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
                }
            }
        }
        if (!isRemoved()) {
            discard();
        }
    }

    private void tickTargeting() {
        bounceAround();
        if (getScaleFactor() == 1) {
            lookForTargets();
            captureCollidingEntities();
        }
        if (getBbHeight() < 3.7) {
            setScaleFactor(getScaleFactor() + 0.01);
        }
        if (getScaleFactor() > 1) {
            setScaleFactor(1);
        }
    }

    private void captureCollidingEntities() {
        List<UUID> captives = getCaptives();
        List<Entity> entities = level().getEntities(this, getBoundingBox());

        for (Entity entity : entities) {
            Optional<BubbleImprisonedData> bubbleData = entity.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
            if (entity instanceof LivingEntity && bubbleData.isEmpty() && !entity.noPhysics) {
                UUID uuid = entity.getUUID();
                if (!captives.contains(uuid) && captives.size() < 3 && uuid != getUUID() && uuid != getOwnerUUID()) {
                    if (level() instanceof ServerLevel serverLevel && getOwnerUUID() != null) {
                        EntityReversionData.get(serverLevel).putRevertible(getOwnerUUID(), entity);
                    }
                    captives.add(uuid);
                    entity.setData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA, Optional.of(new BubbleImprisonedData(getUUID(), getId())));
                    if (captives.size() == 3) {
                        setBehavior(BubbleBehavior.CAPTURING);
                        setDeltaMovement(Vec3.ZERO);
                    }
                }
            }
        }
        setCaptives(captives);
        if (!captives.isEmpty()) {
            targetingToCapturingTickCooldown--;
        }
        if (targetingToCapturingTickCooldown <= 0) {
            setBehavior(BubbleBehavior.CAPTURING);
            setDeltaMovement(Vec3.ZERO);
            hurtMarked = true;
        }
    }

    private void bounceAround() {
        Vec3 velocity = getDeltaMovement().scale(0.9955).add(0, -0.05, 0);

        AABB box = getBoundingBox();

        velocity = handleBounce(velocity, box, velocity.x, 0, 0, Direction.Axis.X);
        velocity = handleBounce(velocity, box, 0, velocity.y, 0, Direction.Axis.Y);
        velocity = handleBounce(velocity, box, 0, 0, velocity.z, Direction.Axis.Z);

        double minSpeed = 0.05;
        if (velocity.lengthSqr() < minSpeed * minSpeed) {
            velocity = velocity.normalize().scale(minSpeed);
        }

        if (bounceCount > MAX_BOUNCES) {
            discard();
        }

        setDeltaMovement(velocity);
        hurtMarked = true;
    }

    private Vec3 handleBounce(Vec3 velocity, AABB box, double dx, double dy, double dz, Direction.Axis axis) {
        if (!level().noCollision(this, box.move(dx, dy, dz))) {
            switch (axis) {
                case X -> velocity = new Vec3(-velocity.x, velocity.y, velocity.z);
                case Y -> velocity = new Vec3(velocity.x, -velocity.y, velocity.z);
                case Z -> velocity = new Vec3(velocity.x, velocity.y, -velocity.z);
            }
            if (target != null && target.isAlive()) {
                velocity = biasBounceTowardTarget(velocity);
            } else {
                double rand = (Math.random() - 0.5) * 0.1;
                switch (axis) {
                    case X -> velocity = velocity.add(rand, 0, 0);
                    case Y -> velocity = velocity.add(0, rand, 0);
                    case Z -> velocity = velocity.add(0, 0, rand);
                }
            }
            switch (axis) {
                case X -> setPos(getX() + Math.signum(dx) * 0.05, getY(), getZ());
                case Y -> setPos(getX(), getY() + Math.signum(dy) * 0.05, getZ());
                case Z -> setPos(getX(), getY(), getZ() + Math.signum(dz) * 0.05);
            }
            bounceCount++;
        }
        return velocity;
    }

    private void lookForTargets() {
        if (retargetCooldown <= 0) {
            retargetCooldown = 10;

            List<LivingEntity> candidates = level().getEntitiesOfClass(
                    LivingEntity.class,
                    getBoundingBox().inflate(8),
                    e -> e.isAlive() && (getOwnerUUID() == null || !getOwnerUUID().equals(e.getUUID())));

            if (!candidates.isEmpty()) {
                target = candidates.stream()
                        .min(Comparator.comparingDouble(e -> e.distanceToSqr(this)))
                        .orElse(null);
            }
        }
        retargetCooldown--;
    }

    private Vec3 biasBounceTowardTarget(Vec3 velocity) {
        if (target == null || !target.isAlive()) return velocity;

        Vec3 toTarget = target.position()
                .add(0, target.getBbHeight() * 0.5, 0)
                .subtract(position())
                .normalize();

        double blendFactor = 0.3;
        Vec3 biased = velocity.normalize().scale(0.8)
                .add(toTarget.scale(blendFactor))
                .normalize();
        return biased;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (getScaleFactor() != 1) {
            return super.getDimensions(pose).scale((float) getScaleFactor());
        }
        return super.getDimensions(pose);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || !source.is(MineraculousKamikotizationsDamageTypeTags.BREAKS_BUBBLE_PRISONS);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_BUBBLE, new BubblePrisonData());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            ownerUUID = compound.getUUID("Owner");
        }
        if (compound.contains("BubbleData")) {
            BubblePrisonData.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, compound.get("BubbleData"))
                    .resultOrPartial(error -> System.err.println("Failed to parse BubbleData: " + error))
                    .ifPresent(data -> this.entityData.set(DATA_BUBBLE, data));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (ownerUUID != null) {
            compound.putUUID("Owner", ownerUUID);
            BubblePrisonData.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, getBubbleData())
                    .resultOrPartial(error -> System.err.println("Failed to encode BubbleData: " + error))
                    .ifPresent(tag -> compound.put("BubbleData", tag));
        }
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwner(Entity entity) {
        this.ownerUUID = entity == null ? null : entity.getUUID();
    }

    public enum BubbleBehavior implements StringRepresentable {
        EXPLODING, // Red
        TARGETING, // Green
        CAPTURING;  // Purple

        public static final Codec<BubbleBehavior> CODEC = StringRepresentable.fromEnum(BubbleBehavior::values);
        public static final StreamCodec<ByteBuf, BubbleBehavior> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(BubbleBehavior::of, BubbleBehavior::getSerializedName);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        public static BubbleBehavior of(String name) {
            return valueOf(name.toUpperCase());
        }
    }
}
