package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsDamageTypeTags;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.BubblePrisonData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.MineraculousKamikotizationsAttachementTypes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BubblePrison extends Entity {
    private int targetingToCapturingTickCooldown = 10;
    private LivingEntity target;
    private int retargetCooldown = 0;
    private int bounceCount = 0;

    private static final int MAX_BOUNCES = 8;
    private static final EntityDataAccessor<BubbleBehavior> DATA_BEHAVIOUR = SynchedEntityData.defineId(BubblePrison.class, MineraculousKamikotizationsEntityDataSerializers.BUBBLE_PRISON_BEHAVIOR.get());
    private static final EntityDataAccessor<List<UUID>> DATA_CAPTIVES = SynchedEntityData.defineId(BubblePrison.class, MineraculousKamikotizationsEntityDataSerializers.BUBBLE_PRISON_CAPTIVES.get());

    public BubblePrison(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
        this.noPhysics = false;
    }

    public BubbleBehavior getBehavior() {
        return entityData.get(DATA_BEHAVIOUR);
    }

    public void setBehavior(BubbleBehavior behavior) {
        entityData.set(DATA_BEHAVIOUR, behavior);
        if (behavior == BubbleBehavior.TARGETING) {
            targetingToCapturingTickCooldown = 20;
        }
    }

    public List<UUID> getCaptives() {
        return new ArrayList<>(entityData.get(DATA_CAPTIVES));
    }

    public void setCaptives(List<UUID> list) {
        entityData.set(DATA_CAPTIVES, list);
    }

    @Override
    public void tick() {
        if (getBehavior() == BubbleBehavior.TARGETING) {
            tickTargeting();
        } else if (getBehavior() == BubbleBehavior.CAPTURING) {
            tickCapturing();
        }

        this.move(MoverType.SELF, getDeltaMovement());
        super.tick();
    }

    public static final double PHYSICAL_RADIUS = 1.2;

    public void constrainCaptiveEntityPosition(LivingEntity captive) {
        Vec3 center = position().add(0, PHYSICAL_RADIUS, 0);
        Vec3 toCaptured = captive.position().subtract(center);
        if (toCaptured.length() > PHYSICAL_RADIUS) {
            captive.resetFallDistance();
            Vec3 correction = toCaptured.normalize().scale(PHYSICAL_RADIUS).subtract(toCaptured);
            captive.move(MoverType.SELF, correction);
            captive.setDeltaMovement(captive.getDeltaMovement().multiply(1, 0.8, 1));
        }
    }

    private void tickCapturing() {
        pushNotCaptiveEntities();
        pushBubbleOnPlayerMovement();
    }

    public static void onCaptiveEntityTick(Level level, Entity entity, Optional<BubblePrisonData> bubbleData) {
        if (level instanceof ServerLevel serverLevel) {
            Entity bubble = serverLevel.getEntity(bubbleData.get().bubbleUUID());
            if (bubble == null || !bubble.isAlive() || bubble.isRemoved()) {
                entity.removeData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
            } else {
                BubblePrisonData updatedData = new BubblePrisonData(bubble.getUUID(), bubble.getId());
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
        Vec3 center = position().add(0, PHYSICAL_RADIUS, 0);
        List<Entity> entities = level().getEntities(this, getBoundingBox());
        for (Entity entity : entities) {
            Optional<BubblePrisonData> bubbleData = entity.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
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
            Vec3 center = position().add(0, PHYSICAL_RADIUS, 0);
            Vec3 totalPush = Vec3.ZERO;
            int pushingPlayers = 0;
            double pushThreshold = PHYSICAL_RADIUS * 0.5;
            for (UUID id : getCaptives()) {
                Entity entity = serverLevel.getEntity(id);
                if (entity instanceof ServerPlayer player) {
                    Vec3 offset = player.position().subtract(center);
                    offset = new Vec3(offset.x, 0, offset.z);
                    double distance = offset.length();
                    if (distance > pushThreshold) {
                        double strength = (distance - pushThreshold) / (PHYSICAL_RADIUS - pushThreshold);
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
        lookForTargets();
        captureCollidingEntities();
    }

    private void captureCollidingEntities() {
        List<UUID> captives = getCaptives(); // copy to mutable list
        List<Entity> entities = level().getEntities(this, getBoundingBox());

        for (Entity entity : entities) {
            Optional<BubblePrisonData> bubbleData = entity.getData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA);
            if (entity instanceof LivingEntity && bubbleData.isEmpty() && !entity.noPhysics) {
                UUID uuid = entity.getUUID();
                if (!captives.contains(uuid) && captives.size() < 3 && uuid != getUUID()) {
                    captives.add(uuid);
                    entity.setData(MineraculousKamikotizationsAttachementTypes.BUBBLE_DATA, Optional.of(new BubblePrisonData(getUUID(), getId())));
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
        Vec3 velocity = getDeltaMovement().add(0, -0.05, 0);

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

        //if (getCaptives().isEmpty()) {
            setDeltaMovement(velocity);
            hurtMarked = true;
        //}
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
                    e -> e.isAlive());

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
        builder.define(DATA_BEHAVIOUR, BubbleBehavior.TARGETING);
        builder.define(DATA_CAPTIVES, List.of());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}

    public enum BubbleBehavior {
        EXPLODING, // Red
        TARGETING, // Green
        CAPTURING  // Purple
    }
}
