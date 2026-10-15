package dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting;

import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.ReversionData;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.CleanseEntityReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.DiscardEntityReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.RestoreBlockReversionEntry;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.ReviveEntityReversionEntry;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class RevertingLightningBolt extends LightningBolt {
    private boolean markedRemovable = false;

    public RevertingLightningBolt(EntityType<? extends LightningBolt> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        ServerPlayer cause = getCause();
        if (cause != null && level() instanceof ServerLevel level) {
            ReversionData reversionData = ReversionData.get(level);
            UUID causeId = cause.getUUID();
            if (!markedRemovable) {
                markedRemovable = true;
                reversionData.putEntry(DiscardEntityReversionEntry.of(causeId, this));
            }
            if (!visualOnly)
                for (Entity entity : level.getEntities(this, new AABB(getX() - 3.0, getY() - 3.0, getZ() - 3.0, getX() + 3.0, getY() + 6.0 + 3.0, getZ() + 3.0), Entity::isAlive))
                    if (entity instanceof LivingEntity livingEntity)
                        reversionData.putEntry(CleanseEntityReversionEntry.of(causeId, livingEntity));
                    else
                        reversionData.putEntry(ReviveEntityReversionEntry.of(causeId, entity));
        }
        super.tick();
    }

    @Override
    protected void spawnFire(int extraIgnitions) {
        ServerPlayer cause = getCause();
        if (!visualOnly && cause != null && level() instanceof ServerLevel level && level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockPos blockPos = blockPosition();
            ReversionData reversionData = ReversionData.get(level);
            UUID causeId = cause.getUUID();

            ignite(blockPos, level, reversionData, causeId);

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            for (int i = 0; i < extraIgnitions; i++) {
                pos.setWithOffset(blockPos, random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
                ignite(pos, level, reversionData, causeId);
            }
        }
    }

    protected void ignite(BlockPos pos, Level level, ReversionData reversionData, UUID causeId) {
        BlockState oldState = level.getBlockState(pos);
        BlockState newState = BaseFireBlock.getState(level, pos);
        if (oldState.isAir() && newState.canSurvive(level, pos)) {
            level.setBlockAndUpdate(pos, newState);
            blocksSetOnFire++;
            reversionData.putEntry(RestoreBlockReversionEntry.of(causeId, level.dimension(), pos, oldState));
        }
    }
}
