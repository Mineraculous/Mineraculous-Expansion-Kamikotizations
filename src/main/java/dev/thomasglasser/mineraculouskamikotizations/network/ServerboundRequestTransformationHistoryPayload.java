package dev.thomasglasser.mineraculouskamikotizations.network;

import dev.thomasglasser.mineraculous.api.network.MineraculousNetworkUtils;
import dev.thomasglasser.mineraculous.api.network.MineraculousPacketPayload;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ServerboundRequestTransformationHistoryPayload implements MineraculousPacketPayload {
    public static final ServerboundRequestTransformationHistoryPayload INSTANCE = new ServerboundRequestTransformationHistoryPayload();
    public static final Type<ServerboundRequestTransformationHistoryPayload> TYPE = new Type<>(MineraculousKamikotizations.modLoc("serverbound_request_transformation_history"));
    public static final StreamCodec<ByteBuf, ServerboundRequestTransformationHistoryPayload> CODEC = StreamCodec.unit(INSTANCE);

    public ServerboundRequestTransformationHistoryPayload() {}

    // ON SERVER
    @Override
    public void handle(Player player) {
        ServerPlayer serverPlayer = (ServerPlayer) player;
        MineraculousNetworkUtils.sendToClient(new ClientboundTransformationEntriesSyncPayload(TransformationHistoryData.get(serverPlayer.serverLevel()).getAllEntries()), serverPlayer);
    }

    @Override
    public Type<? extends MineraculousPacketPayload> type() {
        return TYPE;
    }
}
