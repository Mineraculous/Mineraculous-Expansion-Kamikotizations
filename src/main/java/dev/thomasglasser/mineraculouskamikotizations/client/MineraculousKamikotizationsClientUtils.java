package dev.thomasglasser.mineraculouskamikotizations.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.TransformationEntriesSyncListener;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

public class MineraculousKamikotizationsClientUtils {
    public static final Component GUI_LOADING = Component.translatable("gui.loading");
    public static final Component TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE = MineraculousClientUtils.translatableGui("transformation_entry_selection", "title");

    public static void openTransformationEntrySelectionScreen(ImmutableSet<TransformationHistoryData.TransformationEntry> entries) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        class TransformationEntrySelectionScreenHolder {
            SelectionScreen<TransformationHistoryData.TransformationEntry> screen;
        }

        TransformationEntrySelectionScreenHolder holder = new TransformationEntrySelectionScreenHolder();
        holder.screen = new SelectionScreen<>(
                TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE,
                null,
                false,
                null,
                ImmutableList.copyOf(entries),
                MineraculousKamikotizationsClientUtils::getTransformationEntryDisplayName,
                IconRenderers.MIRACULOUS.map(MineraculousKamikotizationsClientUtils::getTransformationEntryMiraculousHolder),
                transformationEntry -> {

                });
        minecraft.setScreen(holder.screen);
    }

    private static Component getTransformationEntryDisplayName(TransformationHistoryData.TransformationEntry entry) {
        return entry.name();
    }

    private static Holder<Miraculous> getTransformationEntryMiraculousHolder(TransformationHistoryData.TransformationEntry entry) {
        return entry.miraculous();
    }

    public static void triggerTransformationEntriesSyncListener(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries) {
        if (Minecraft.getInstance().screen instanceof TransformationEntriesSyncListener tracker) {
            tracker.onTransformationEntrySync(transformationEntries);
        }
    }
}
