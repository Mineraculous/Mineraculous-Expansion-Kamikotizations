package dev.thomasglasser.mineraculouskamikotizations.network;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData.TransformationEntry;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record ClientBoundOpenTransformationHistoryScreenPayload(UUID targetPlayerUuid, Collection<TransformationEntry> entries) implements ExtendedPacketPayload {
    public static final Type<ClientBoundOpenTransformationHistoryScreenPayload> TYPE = new Type<>(MineraculousKamikotizations.modLoc("clientbound_open_transformation_history_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundOpenTransformationHistoryScreenPayload> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ClientBoundOpenTransformationHistoryScreenPayload::targetPlayerUuid,
            ByteBufCodecs.collection(ArrayList::new, TransformationEntry.STREAM_CODEC),
            ClientBoundOpenTransformationHistoryScreenPayload::entries,
            ClientBoundOpenTransformationHistoryScreenPayload::new);

    @Override
    public void handle(Player player) {
        MineraculousKamikotizationsClientUtils.openPlayerSelectionScreen(targetPlayerUuid, entries);
    }

    @Override
    public Type<? extends ExtendedPacketPayload> type() {
        return TYPE;
    }
}
