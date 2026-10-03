package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class TransformationEntriesLoadingScreen extends LoadingScreen implements TransformationEntriesSyncListener {
    public static final Component TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE = MineraculousClientUtils.translatableGui("transformation_entry_selection", "title");
    protected final Consumer<TransformationHistoryData.TransformationEntry> onSelected;
    protected final @Nullable TransformationHistoryData.TransformationEntry initialEntry;
    protected final @Nullable Holder<Miraculous> miraculous;
    protected final Button entryButton;
    private boolean canceled;

    public TransformationEntriesLoadingScreen(Screen parentScreen, Consumer<TransformationHistoryData.TransformationEntry> onSelected, @Nullable TransformationHistoryData.TransformationEntry initialEntry, @Nullable Holder<Miraculous> miraculous, Button entryButton) {
        super(parentScreen);
        this.onSelected = onSelected;
        this.initialEntry = initialEntry;
        this.miraculous = miraculous;
        this.entryButton = entryButton;
    }

    @Override
    public void onClose() {
        this.canceled = true;
        this.resetButton();
        super.onClose();
    }

    @Override
    public void onTransformationEntriesSynced(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries) {
        if (this.canceled || this.minecraft == null || this.minecraft.player == null)
            return;

        ImmutableList.Builder<TransformationHistoryData.TransformationEntry> builder = ImmutableList.builderWithExpectedSize(transformationEntries.size());
        for (TransformationHistoryData.TransformationEntry entry : transformationEntries)
            if (this.miraculous == null || this.miraculous.equals(entry.miraculous()))
                builder.add(entry);
        this.minecraft.setScreen(new SelectionScreen<>(TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE, this.parent, false, this.initialEntry, builder.build(), TransformationHistoryData.TransformationEntry::getName, IconRenderers.MIRACULOUS.map(TransformationHistoryData.TransformationEntry::miraculous), selectedEntry -> {
            this.onSelected.accept(selectedEntry);
            if (selectedEntry != null) {
                this.entryButton.setMessage(selectedEntry.getName());
                this.entryButton.active = true;
            } else {
                this.resetButton();
            }
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.parent);
            }
        }));
    }

    private void resetButton() {
        this.entryButton.setMessage(this.initialEntry != null ? this.initialEntry.getName() : MineraculousClientUtils.GUI_CHOOSE);
        this.entryButton.active = true;
    }
}
