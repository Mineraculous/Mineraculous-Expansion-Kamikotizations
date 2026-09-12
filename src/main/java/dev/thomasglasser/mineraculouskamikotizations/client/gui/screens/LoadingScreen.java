package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class LoadingScreen extends Screen {
    protected final Screen parent;
    private long lastNarration = -1L;
    private boolean done;

    protected LoadingScreen(Screen parent) {
        super(GameNarrator.NO_TITLE);
        this.parent = parent;
    }

    @Override
    public void removed() {
        this.done = true;
        this.triggerImmediateNarration(true);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        long currentTimeMs = Util.getMillis();
        if (currentTimeMs - lastNarration > LevelLoadingScreen.NARRATION_DELAY_MS) {
            this.lastNarration = currentTimeMs;
            this.triggerImmediateNarration(true);
        }
    }

    @Override
    protected void updateNarratedWidget(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, this.done ? Component.translatable("narrator.loading.done") : MineraculousKamikotizationsClientUtils.GUI_LOADING);
    }

    @Override
    public void onClose() {
        this.getMinecraft().setScreen(this.parent);
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build());
    }
}
