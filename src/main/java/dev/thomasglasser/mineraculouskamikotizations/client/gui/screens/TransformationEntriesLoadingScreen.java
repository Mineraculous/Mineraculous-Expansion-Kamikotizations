package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

public class TransformationEntriesLoadingScreen extends LoadingScreen implements TransformationEntriesSyncListener {
    protected final Consumer<TransformationHistoryData.TransformationEntry> transformationHistoryDataConsumer;
    protected final TransformationHistoryData.TransformationEntry transformationEntry;

    public TransformationEntriesLoadingScreen(Screen parent, Consumer<TransformationHistoryData.TransformationEntry> transformationHistoryDataConsumer, TransformationHistoryData.TransformationEntry transformationEntry) {
        super(parent);
        this.transformationHistoryDataConsumer = transformationHistoryDataConsumer;
        this.transformationEntry = transformationEntry;
    }

    @Override
    public void onTransformationEntrySync(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries) {
        if (getMinecraft().player == null) return;
        class TransformationEntrySelectionScreenHolder {
            SelectionScreen<TransformationHistoryData.TransformationEntry> screen;
        }

        TransformationEntrySelectionScreenHolder holder = new TransformationEntrySelectionScreenHolder();
        holder.screen = new SelectionScreen<>(
                MineraculousKamikotizationsClientUtils.TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE,
                parent,
                false,
                transformationEntry,
                ImmutableList.copyOf(transformationEntries),
                TransformationEntriesLoadingScreen::getTransformationEntryDisplayName,
                IconRenderers.MIRACULOUS.map(TransformationEntriesLoadingScreen::getTransformationEntryMiraculousHolder),
                transformationHistoryDataConsumer);
        getMinecraft().setScreen(holder.screen);
    }

    private static Component getTransformationEntryDisplayName(TransformationHistoryData.TransformationEntry entry) {
        return entry.name();
    }

    private static Holder<Miraculous> getTransformationEntryMiraculousHolder(TransformationHistoryData.TransformationEntry entry) {
        return entry.miraculous();
    }
}
