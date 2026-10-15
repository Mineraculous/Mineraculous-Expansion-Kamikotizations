package dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting;

import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.ReversionData;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.CleanseEntityReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.DiscardEntityReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.RestoreBlockReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.ReviveEntityReversionEntry;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.projectile.IceCharge;
import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class RevertingIceCharge extends IceCharge {
    public RevertingIceCharge(EntityType<? extends IceCharge> entityType, Level level) {
        super(entityType, level);
    }

    public RevertingIceCharge(EntityType<? extends IceCharge> entityType, @Nullable Entity owner, double x, double y, double z, Level level) {
        super(entityType, owner, x, y, z, level);
        if (level instanceof ServerLevel serverLevel && owner != null)
            ReversionData.get(serverLevel).putEntry(DiscardEntityReversionEntry.of(owner.getUUID(), this));
    }

    public RevertingIceCharge(@Nullable Entity owner, double x, double y, double z, Level level) {
        super(MineraculousKamikotizationsEntityTypes.REVERTING_ICE_CHARGE.get(), owner, x, y, z, level);
        if (level instanceof ServerLevel serverLevel && owner != null)
            ReversionData.get(serverLevel).putEntry(DiscardEntityReversionEntry.of(owner.getUUID(), this));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity owner = getOwner();
        if (owner != null && level() instanceof ServerLevel level) {
            UUID cause = owner.getUUID();
            Entity entity = result.getEntity();
            ReversionData reversionData = ReversionData.get(level);
            if (entity instanceof LivingEntity livingEntity)
                reversionData.putEntry(CleanseEntityReversionEntry.of(cause, livingEntity));
            else
                reversionData.putEntry(ReviveEntityReversionEntry.of(cause, entity));
            ResourceKey<Level> dimension = level.dimension();
            applyToSurroundingBlocks(entity, level, (pos, state) -> reversionData.putEntry(RestoreBlockReversionEntry.of(cause, dimension, pos, state)));
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Entity owner = getOwner();
        if (owner != null && level() instanceof ServerLevel level) {
            UUID cause = owner.getUUID();
            ReversionData reversionData = ReversionData.get(level);
            ResourceKey<Level> dimension = level.dimension();
            applyToSurroundingBlocks(result.getBlockPos(), level, (pos, state) -> reversionData.putEntry(RestoreBlockReversionEntry.of(cause, dimension, pos, state)));
        }
        super.onHitBlock(result);
    }
}
