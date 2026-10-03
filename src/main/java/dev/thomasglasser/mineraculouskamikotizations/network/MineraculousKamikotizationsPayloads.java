package dev.thomasglasser.mineraculouskamikotizations.network;

import com.google.common.collect.ImmutableList;
import dev.thomasglasser.mineraculous.api.network.MineraculousPacketPayload;
import dev.thomasglasser.mineraculous.api.network.PayloadInfo;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class MineraculousKamikotizationsPayloads {
    private static final ImmutableList<PayloadInfo<?>> PAYLOADS = ImmutableList.of(
            // Serverbound
            new PayloadInfo<>(ServerboundRequestTransformationHistoryPayload.TYPE, MineraculousPacketPayload.Direction.CLIENT_TO_SERVER, ServerboundRequestTransformationHistoryPayload.CODEC),

            // Clientbound
            new PayloadInfo<>(ClientboundTransformationEntriesSyncPayload.TYPE, MineraculousPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundTransformationEntriesSyncPayload.CODEC)

    );

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MineraculousKamikotizations.MOD_ID);
        PAYLOADS.forEach(info -> info.register(registrar));
    }
}
