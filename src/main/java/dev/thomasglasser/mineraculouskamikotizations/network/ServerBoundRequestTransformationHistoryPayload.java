package dev.thomasglasser.mineraculouskamikotizations.network;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record ServerBoundRequestTransformationHistoryPayload(Optional<UUID> targetPlayerUuid) implements ExtendedPacketPayload {
    public static final Type<ServerBoundRequestTransformationHistoryPayload> TYPE = new Type<>(MineraculousKamikotizations.modLoc("serverbound_request_transformation_history"));

    public static final StreamCodec<ByteBuf, ServerBoundRequestTransformationHistoryPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC),
            ServerBoundRequestTransformationHistoryPayload::targetPlayerUuid,
            ServerBoundRequestTransformationHistoryPayload::new);

    public ServerBoundRequestTransformationHistoryPayload(UUID targetPlayerUuid) {
        this(Optional.ofNullable(targetPlayerUuid));
    }

    // ON SERVER
    @Override
    public void handle(Player player) {
        if (player instanceof ServerPlayer serverPlayer && player.level() instanceof ServerLevel serverLevel) {
            UUID uuid = targetPlayerUuid.orElseGet(serverPlayer::getUUID);
            Collection<TransformationHistoryData.TransformationEntry> entries = TransformationHistoryData.get(serverLevel).getHistory(uuid);
            TommyLibServices.NETWORK.sendToClient(new ClientBoundOpenTransformationHistoryScreenPayload(uuid, entries), serverPlayer);
        }
    }

    @Override
    public Type<? extends ExtendedPacketPayload> type() {
        return TYPE;
    }
}
