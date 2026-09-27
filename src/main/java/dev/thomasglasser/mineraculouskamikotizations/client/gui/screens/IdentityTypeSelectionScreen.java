package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.HolderSelectionScreen;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.network.ServerboundRequestTransformationHistoryPayload;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
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
    private final @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> initialValue;
    private final Consumer<@Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> consumer;

    public IdentityTypeSelectionScreen(
            Screen parentScreen,
            boolean allowNone,
            @Nullable Holder<Miraculous> miraculous,
            @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> initialValue,
            Consumer<@Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> consumer) {
        super(MineraculousKamikotizationsClientUtils.IDENTITY_TYPE_SELECTION_SCREEN_TITLE);
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
        int centerX = (this.width - buttonWidth) / 2;
        int startY = (this.height / 2) - 25;

        this.addRenderableWidget(Button.builder(
                MineraculousKamikotizationsClientUtils.IDENTITY_TYPE_SELECTION_SCREEN_PLAYER,
                this::openPlayerMiraculousSelection)
                .bounds(centerX, startY, buttonWidth, buttonHeight)
                .build());

        this.addRenderableWidget(Button.builder(
                MineraculousKamikotizationsClientUtils.IDENTITY_TYPE_SELECTION_SCREEN_TRANSFORMATION_ENTRY,
                this::openMiraculousSelection)
                .bounds(centerX, startY + 25, buttonWidth, buttonHeight)
                .build());
    }

    private void openPlayerMiraculousSelection(Button entryButton) {
        Minecraft.getInstance().setScreen(new HolderSelectionScreen<>(
                parentScreen,
                allowNone,
                miraculous,
                MineraculousRegistries.MIRACULOUS,
                holder -> Component.literal(holder.getRegisteredName()),
                IconRenderers.MIRACULOUS,
                selectedMiraculous -> {
                    if (selectedMiraculous == null) {
                        if (allowNone) {
                            consumer.accept(null);
                        }
                        Minecraft.getInstance().setScreen(parentScreen);
                        return;
                    }

                    UUID initialUuid = (initialValue != null && initialValue.left().isPresent()) ? initialValue.left().get().getSecond() : null;

                    Minecraft.getInstance().setScreen(new PlayerSelectionScreen(
                            parentScreen,
                            allowNone,
                            initialUuid,
                            selectedUuid -> {
                                if (selectedUuid != null) {
                                    consumer.accept(Either.left(Pair.of(selectedMiraculous, selectedUuid)));
                                } else {
                                    consumer.accept(null);
                                }
                                Minecraft.getInstance().setScreen(parentScreen);
                            }));
                }));
    }

    private void openMiraculousSelection(Button entryButton) {
        Minecraft.getInstance().setScreen(new HolderSelectionScreen<>(
                parentScreen,
                allowNone,
                miraculous,
                MineraculousRegistries.MIRACULOUS,
                holder -> Component.literal(holder.getRegisteredName()),
                IconRenderers.MIRACULOUS,
                selectedMiraculous -> {
                    if (selectedMiraculous == null) {
                        if (allowNone) {
                            consumer.accept(null);
                        }
                        Minecraft.getInstance().setScreen(parentScreen);
                        return;
                    }

                    TransformationHistoryData.TransformationEntry initialEntry = (initialValue != null && initialValue.right().isPresent()) ? initialValue.right().get() : null;

                    Minecraft.getInstance().setScreen(new TransformationEntriesLoadingScreen(
                            parentScreen,
                            entry -> {
                                consumer.accept(entry != null ? Either.right(entry) : null);
                                Minecraft.getInstance().setScreen(parentScreen);
                            },
                            initialEntry,
                            selectedMiraculous,
                            entryButton));
                    TommyLibServices.NETWORK.sendToServer(ServerboundRequestTransformationHistoryPayload.INSTANCE);
                }));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, (this.height / 2) - 50, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parentScreen);
        }
    }
}
