package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsDamageTypeTags;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BubblePrison extends Entity {
    private float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;

    private LivingEntity target;
    private int retargetCooldown = 0;
    private int bounceCount = 0;

    private static final int MAX_BOUNCES = 8;
    private static final double SPEED = 0.5;

    private static final EntityDataAccessor<BubbleBehavior> DATA_BEHAVIOUR = SynchedEntityData.defineId(BubblePrison.class, MineraculousKamikotizationsEntityDataSerializers.BUBBLE_PRISON_BEHAVIOR.get());

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
    }

    @Override
    public void tick() {
        tickTargeting();
        this.move(MoverType.SELF, getDeltaMovement());

        super.tick();
    }

    private void tickTargeting() {
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

        setDeltaMovement(velocity);
        hurtMarked = true;
        lookForTargets();
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
        Vec3 biased = velocity.normalize().scale(0.7)
                .add(toTarget.scale(blendFactor))
                .normalize()
                .scale(0.89);
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
    public boolean canCollideWith(Entity entity) {
        return entity.canBeCollidedWith();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yRot;
        this.lerpXRot = xRot;
        this.lerpSteps = 10;
    }

    @Override
    public double lerpTargetX() {
        return this.lerpSteps > 0 ? this.lerpX : this.getX();
    }

    @Override
    public double lerpTargetY() {
        return this.lerpSteps > 0 ? this.lerpY : this.getY();
    }

    @Override
    public double lerpTargetZ() {
        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
    }

    @Override
    public float lerpTargetXRot() {
        return this.lerpSteps > 0 ? (float) this.lerpXRot : this.getXRot();
    }

    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? (float) this.lerpYRot : this.getYRot();
    }

    @Override
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_BEHAVIOUR, BubbleBehavior.TARGETING);
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
