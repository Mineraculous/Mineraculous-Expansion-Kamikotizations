package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.mojang.datafixers.util.Either;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.targetvisual.VisualTarget;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class IdentityTypeSelectionScreen extends Screen {
    private final Screen parentScreen;
    private final boolean allowNone;
    private final @Nullable Holder<Miraculous> miraculous;
    private final @Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry> initialValue;
    private final Consumer<@Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry>> consumer;

    public static final Component IDENTITY_TYPE_SELECTION_SCREEN_TITLE = MineraculousClientUtils.translatableGui("identity_type_selection", "title");
    public static final Component IDENTITY_TYPE_SELECTION_SCREEN_PLAYER = MineraculousClientUtils.translatableGui("identity_type_selection", "player");
    public static final Component IDENTITY_TYPE_SELECTION_SCREEN_TRANSFORMATION_ENTRY = MineraculousClientUtils.translatableGui("identity_type_selection", "transformation_entry");

    public IdentityTypeSelectionScreen(Screen parentScreen, boolean allowNone, @Nullable Holder<Miraculous> miraculous, @Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry> initialValue, Consumer<@Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry>> consumer) {
        super(IDENTITY_TYPE_SELECTION_SCREEN_TITLE);
        this.parentScreen = parentScreen;
        this.allowNone = allowNone;
        this.miraculous = miraculous;
        this.initialValue = initialValue;
        this.consumer = consumer;
    }

    @Override
    protected void init() {
        int buttonWidth = 160;
        int buttonHeight = 20;
        int centerX = (width - buttonWidth) / 2;
        int startY = (height / 2) - 25;
        this.addRenderableWidget(Button.builder(IDENTITY_TYPE_SELECTION_SCREEN_PLAYER, this::openPlayerMiraculousSelection).bounds(centerX, startY, buttonWidth, buttonHeight).build());
        this.addRenderableWidget(Button.builder(IDENTITY_TYPE_SELECTION_SCREEN_TRANSFORMATION_ENTRY, this::openMiraculousSelection).bounds(centerX, startY + 25, buttonWidth, buttonHeight).build());
    }

    private void openPlayerMiraculousSelection(Button entryButton) {
        MineraculousKamikotizationsClientUtils.openPlayerMiraculousSelection(parentScreen, allowNone, miraculous, initialValue, consumer);
    }

    private void openMiraculousSelection(Button entryButton) {
        MineraculousKamikotizationsClientUtils.openTransformationEntryMiraculousSelection(parentScreen, allowNone, miraculous, initialValue, entryButton, consumer);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, title, width / 2, (height / 2) - 50, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            this.minecraft.setScreen(parentScreen);
        }
    }
}
