package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;

public class TransformationEntriesLoadingScreen extends LoadingScreen implements TransformationEntriesSyncListener {
    protected final Consumer<TransformationHistoryData.TransformationEntry> onSelected;
    protected final TransformationHistoryData.TransformationEntry current;
    protected final Button entryButton;
    private boolean canceled;

    public TransformationEntriesLoadingScreen(Screen parent, Consumer<TransformationHistoryData.TransformationEntry> transformationHistoryDataConsumer, TransformationHistoryData.TransformationEntry transformationEntry, Button widgetButton) {
        super(parent);
        this.onSelected = transformationHistoryDataConsumer;
        this.current = transformationEntry;
        this.entryButton = widgetButton;
    }

    @Override
    public void onClose() {
        this.canceled = true;
        resetButton();
        super.onClose();
    }

    @Override
    public void onTransformationEntriesSync(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries) {
        if (canceled || minecraft == null || minecraft.player == null)
            return;
        minecraft.setScreen(new SelectionScreen<>(
                MineraculousKamikotizationsClientUtils.TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE,
                parent,
                false,
                current,
                ImmutableList.copyOf(transformationEntries),
                TransformationHistoryData.TransformationEntry::getName,
                IconRenderers.MIRACULOUS.map(TransformationHistoryData.TransformationEntry::miraculous),
                selectedEntry -> {
                    onSelected.accept(selectedEntry);
                    if (selectedEntry != null) {
                        entryButton.setMessage(selectedEntry.getName());
                        entryButton.active = true;
                    } else {
                        resetButton();
                    }
                    getMinecraft().setScreen(parent);
                }));
    }

    private void resetButton() {
        entryButton.setMessage(MineraculousClientUtils.GUI_CHOOSE);
        entryButton.active = true;
    }
}
