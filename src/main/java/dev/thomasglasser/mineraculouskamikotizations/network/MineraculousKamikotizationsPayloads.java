package dev.thomasglasser.mineraculouskamikotizations.network;

import com.google.common.collect.ImmutableList;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.NeoForgeNetworkUtils;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class MineraculousKamikotizationsPayloads {
    public static final ImmutableList<PayloadInfo<?>> PAYLOADS = ImmutableList.of(
            // Serverbound
            new PayloadInfo<>(ServerboundRequestTransformationHistoryPayload.TYPE, ExtendedPacketPayload.Direction.CLIENT_TO_SERVER, ServerboundRequestTransformationHistoryPayload.CODEC),

            // Clientbound
            new PayloadInfo<>(ClientboundTransformationEntriesSyncPayload.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundTransformationEntriesSyncPayload.CODEC)

    );

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MineraculousKamikotizations.MOD_ID);
        PAYLOADS.forEach(info -> NeoForgeNetworkUtils.register(registrar, info));
    }
}
