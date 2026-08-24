package dev.thomasglasser.mineraculouskamikotizations.network;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData.TransformationEntry;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record ClientBoundOpenTransformationHistoryScreenPayload(Collection<TransformationEntry> entries) implements ExtendedPacketPayload {
    public static final Type<ClientBoundOpenTransformationHistoryScreenPayload> TYPE = new Type<>(MineraculousKamikotizations.modLoc("clientbound_open_transformation_history_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundOpenTransformationHistoryScreenPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ObjectArrayList::new, TransformationEntry.STREAM_CODEC),
            ClientBoundOpenTransformationHistoryScreenPayload::entries,
            ClientBoundOpenTransformationHistoryScreenPayload::new);

    @Override
    public void handle(Player player) {
        MineraculousKamikotizationsClientUtils.openPlayerSelectionScreen(entries);
    }

    @Override
    public Type<? extends ExtendedPacketPayload> type() {
        return TYPE;
    }
}
