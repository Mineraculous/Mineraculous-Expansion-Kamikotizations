package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

public class TransformationEntriesLoadingScreen extends LoadingScreen implements TransformationEntriesSyncListener {
    protected final Consumer<TransformationHistoryData.TransformationEntry> transformationHistoryDataConsumer;
    protected final TransformationHistoryData.TransformationEntry transformationEntry;
    protected final Button widgetButton;
    private boolean cancelled = false;

    public TransformationEntriesLoadingScreen(Screen parent, Consumer<TransformationHistoryData.TransformationEntry> transformationHistoryDataConsumer, TransformationHistoryData.TransformationEntry transformationEntry, Button widgetButton) {
        super(parent);
        this.transformationHistoryDataConsumer = transformationHistoryDataConsumer;
        this.transformationEntry = transformationEntry;
        this.widgetButton = widgetButton;
    }

    @Override
    public void onClose() {
        this.cancelled = true;
        resetButton();
        super.onClose();
    }

    @Override
    public void onTransformationEntrySync(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries) {
        if (cancelled || getMinecraft().player == null) return;
        getMinecraft().setScreen(new SelectionScreen<>(
                MineraculousKamikotizationsClientUtils.TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE,
                parent,
                false,
                transformationEntry,
                ImmutableList.copyOf(transformationEntries),
                TransformationEntriesLoadingScreen::getTransformationEntryDisplayName,
                IconRenderers.MIRACULOUS.map(TransformationEntriesLoadingScreen::getTransformationEntryMiraculousHolder),
                selectedEntry -> {
                    transformationHistoryDataConsumer.accept(selectedEntry);
                    if (selectedEntry != null) {
                        widgetButton.setMessage(selectedEntry.getName());
                        widgetButton.active = true;
                    } else {
                        resetButton();
                    }
                    getMinecraft().setScreen(parent);
                }));
    }

    private static Component getTransformationEntryDisplayName(TransformationHistoryData.TransformationEntry entry) {
        return entry.getName();
    }

    private static Holder<Miraculous> getTransformationEntryMiraculousHolder(TransformationHistoryData.TransformationEntry entry) {
        return entry.miraculous();
    }

    private void resetButton() {
        widgetButton.setMessage(MineraculousClientUtils.GUI_CHOOSE);
        widgetButton.active = true;
    }
}
