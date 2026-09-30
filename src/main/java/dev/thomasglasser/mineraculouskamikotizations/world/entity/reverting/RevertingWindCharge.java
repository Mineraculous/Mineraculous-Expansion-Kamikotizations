package dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting;

import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.ReversionData;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.CleanseEntityReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.DiscardEntityReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.ReviveEntityReversionEntry;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.Level;
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
        moveTo(x, y, z, getYRot(), getXRot());
        if (level instanceof ServerLevel serverLevel && owner != null)
            ReversionData.get(serverLevel).putEntry(DiscardEntityReversionEntry.of(owner.getUUID(), this));
    }

    public RevertingWindCharge(Level level, double x, double y, double z, Vec3 movement) {
        super(MineraculousKamikotizationsEntityTypes.REVERTING_WIND_CHARGE.get(), level);
        setPos(x, y, z);
        moveTo(x, y, z, getYRot(), getXRot());
        reapplyPosition();
        setDeltaMovement(movement.normalize().scale(accelerationPower));
        hasImpulse = true;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity owner = getOwner();
        if (level() instanceof ServerLevel level && owner != null) {
            Entity entity = result.getEntity();
            if (entity instanceof LivingEntity livingEntity)
                ReversionData.get(level).putEntry(CleanseEntityReversionEntry.of(owner.getUUID(), livingEntity, true));
            else
                ReversionData.get(level).putEntry(ReviveEntityReversionEntry.of(owner.getUUID(), entity));
        }
        super.onHitEntity(result);
    }
}
