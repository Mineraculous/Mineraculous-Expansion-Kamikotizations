package dev.thomasglasser.mineraculouskamikotizations.network;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.PlayerSelectionScreen;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData.TransformationEntry;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record ClientBoundOpenTransformationHistoryScreenPayload(UUID targetPlayerUuid, Collection<TransformationEntry> entries) implements ExtendedPacketPayload {
    public static final Type<ClientBoundOpenTransformationHistoryScreenPayload> TYPE = new Type<>(MineraculousKamikotizations.modLoc("clientbound_open_transformation_history_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundOpenTransformationHistoryScreenPayload> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ClientBoundOpenTransformationHistoryScreenPayload::targetPlayerUuid,
            ByteBufCodecs.collection(ArrayList::new, TransformationEntry.STREAM_CODEC),
            ClientBoundOpenTransformationHistoryScreenPayload::entries,
            ClientBoundOpenTransformationHistoryScreenPayload::new);

    private static @Nullable BiConsumer<UUID, TransformationEntry> activeSelectConsumer;
    private static @Nullable TransformationEntry lastSelectedEntry;

    public static void setSelectConsumer(BiConsumer<UUID, TransformationEntry> consumer) {
        activeSelectConsumer = consumer;
    }

    public static @Nullable TransformationEntry getLastSelectedEntry() {
        return lastSelectedEntry;
    }

    // ON CLIENT
    @Override
    public void handle(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        Screen currentScreen = minecraft.screen;

        minecraft.setScreen(new PlayerSelectionScreen(
                currentScreen,
                true,
                null,
                entries,
                targetPlayerUuid,
                selectedEntry -> {
                    lastSelectedEntry = selectedEntry;
                    if (activeSelectConsumer != null) {
                        activeSelectConsumer.accept(selectedEntry != null ? targetPlayerUuid : null, selectedEntry);
                        activeSelectConsumer = null;
                    }
                    minecraft.setScreen(currentScreen);
                }));
    }

    @Override
    public Type<? extends ExtendedPacketPayload> type() {
        return TYPE;
    }
}
