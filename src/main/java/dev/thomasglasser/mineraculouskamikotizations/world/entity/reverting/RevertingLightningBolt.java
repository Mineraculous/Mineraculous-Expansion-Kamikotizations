package dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting;

import dev.thomasglasser.mineraculous.api.world.level.storage.BlockReversionData;
import dev.thomasglasser.mineraculous.api.world.level.storage.EntityReversionData;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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
        if (level() instanceof ServerLevel level && cause != null) {
            EntityReversionData entityReversionData = EntityReversionData.get(level);
            UUID causeId = cause.getUUID();
            if (!markedRemovable) {
                markedRemovable = true;
                entityReversionData.putRemovable(causeId, this);
            }
            if (!visualOnly) {
                List<Entity> hit = level
                        .getEntities(
                                this,
                                new AABB(getX() - 3.0, getY() - 3.0, getZ() - 3.0, getX() + 3.0, getY() + 6.0 + 3.0, getZ() + 3.0),
                                Entity::isAlive);
                for (Entity entity : hit) {
                    entityReversionData.putRevertible(causeId, entity);
                }
            }
        }
        super.tick();
    }

    @Override
    protected void spawnFire(int extraIgnitions) {
        ServerPlayer cause = getCause();
        if (!visualOnly && level() instanceof ServerLevel level && cause != null && level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockReversionData blockReversionData = BlockReversionData.get(level);
            UUID causeId = cause.getUUID();
            ResourceKey<Level> dimension = level.dimension();
            BlockPos pos = blockPosition();
            BlockState oldState = level.getBlockState(pos);
            BlockState newState = BaseFireBlock.getState(level, pos);
            if (oldState.isAir() && newState.canSurvive(level, pos)) {
                level.setBlockAndUpdate(pos, newState);
                blocksSetOnFire++;
                blockReversionData.putRevertible(causeId, dimension, pos, oldState);
            }

            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int i = 0; i < extraIgnitions; i++) {
                mutable.setWithOffset(pos, random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
                oldState = level.getBlockState(mutable);
                newState = BaseFireBlock.getState(level, mutable);
                if (oldState.isAir() && newState.canSurvive(level, mutable)) {
                    level.setBlockAndUpdate(mutable, newState);
                    blocksSetOnFire++;
                    blockReversionData.putRevertible(causeId, dimension, mutable, oldState);
                }
            }
        }
    }
}
