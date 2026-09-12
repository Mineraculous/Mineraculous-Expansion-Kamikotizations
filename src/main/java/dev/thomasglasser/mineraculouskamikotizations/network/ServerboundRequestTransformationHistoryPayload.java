package dev.thomasglasser.mineraculouskamikotizations.network;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ServerboundRequestTransformationHistoryPayload implements ExtendedPacketPayload {
    public static final ServerboundRequestTransformationHistoryPayload INSTANCE = new ServerboundRequestTransformationHistoryPayload();
    public static final Type<ServerboundRequestTransformationHistoryPayload> TYPE = new Type<>(MineraculousKamikotizations.modLoc("serverbound_request_transformation_history"));
    public static final StreamCodec<ByteBuf, ServerboundRequestTransformationHistoryPayload> CODEC = StreamCodec.unit(INSTANCE);

    private ServerboundRequestTransformationHistoryPayload() {}

    @Override
    public void handle(Player player) {
        ServerPlayer serverPlayer = (ServerPlayer) player;
        TommyLibServices.NETWORK.sendToClient(new ClientboundTransformationEntriesSyncPayload(TransformationHistoryData.get(serverPlayer.serverLevel()).getAllEntries()), serverPlayer);
    }

    @Override
    public Type<? extends ExtendedPacketPayload> type() {
        return TYPE;
    }
}
