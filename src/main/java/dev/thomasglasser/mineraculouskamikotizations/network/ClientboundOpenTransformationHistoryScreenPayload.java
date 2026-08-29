package dev.thomasglasser.mineraculouskamikotizations.network;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData.TransformationEntry;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import java.util.ArrayList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record ClientboundOpenTransformationHistoryScreenPayload(ImmutableSet<TransformationEntry> entries) implements ExtendedPacketPayload {
    public static final Type<ClientboundOpenTransformationHistoryScreenPayload> TYPE = new Type<>(MineraculousKamikotizations.modLoc("clientbound_open_transformation_history_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenTransformationHistoryScreenPayload> CODEC = StreamCodec.composite(
            TransformationEntry.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ImmutableSet::copyOf, ArrayList::new),
            ClientboundOpenTransformationHistoryScreenPayload::entries,
            ClientboundOpenTransformationHistoryScreenPayload::new);

    @Override
    public void handle(Player player) {
        MineraculousKamikotizationsClientUtils.triggerTransformationEntriesSyncListener(entries);
    }

    @Override
    public Type<? extends ExtendedPacketPayload> type() {
        return TYPE;
    }
}
