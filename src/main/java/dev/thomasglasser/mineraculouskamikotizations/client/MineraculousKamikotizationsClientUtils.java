package dev.thomasglasser.mineraculouskamikotizations.client;

import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.PlayerSelectionScreen;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.client.Minecraft;

public class MineraculousKamikotizationsClientUtils {
    public static void openPlayerSelectionScreen(UUID targetPlayerUuid, Collection<TransformationHistoryData.TransformationEntry> entries) {
        Minecraft.getInstance().setScreen(new PlayerSelectionScreen(
                null,
                false,
                null,
                entries,
                targetPlayerUuid,
                entry -> {}));
    }
}
