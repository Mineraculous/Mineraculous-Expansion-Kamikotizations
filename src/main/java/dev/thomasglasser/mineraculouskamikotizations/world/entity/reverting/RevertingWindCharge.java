package dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting;

import dev.thomasglasser.mineraculous.api.world.level.storage.EntityReversionData;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RevertingWindCharge extends WindCharge {
    public RevertingWindCharge(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }

    public RevertingWindCharge(@Nullable Entity owner, Level level, double x, double y, double z) {
        super(MineraculousKamikotizationsEntityTypes.REVERTING_WIND_CHARGE.get(), level);
        setOwner(owner);
        this.moveTo(x, y, z, this.getYRot(), this.getXRot());
    }

    public RevertingWindCharge(Level level, double x, double y, double z, Vec3 movement) {
        super(MineraculousKamikotizationsEntityTypes.REVERTING_WIND_CHARGE.get(), level);
        setPos(x, y, z);
        this.moveTo(x, y, z, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        this.setDeltaMovement(movement.normalize().scale(accelerationPower));
        this.hasImpulse = true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity owner = getOwner();
        if (level() instanceof ServerLevel level && owner != null) {
            EntityReversionData.get(level).putRevertible(owner.getUUID(), result.getEntity());
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // TODO: Track block changes
        super.onHitBlock(result);
    }
}
