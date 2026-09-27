package dev.thomasglasser.mineraculouskamikotizations.client.gui.components;

import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.HolderSelectionScreen;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.TransformationEntriesLoadingScreen;
import dev.thomasglasser.mineraculouskamikotizations.network.ServerboundRequestTransformationHistoryPayload;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/// A button that displays the selected {@link TransformationHistoryData.TransformationEntry} and opens the identity type selection flow when clicked.
public class PowerTargetSelectionButton extends Button {
    private final Screen parentScreen;
    private final boolean allowNone;
    private final @Nullable Holder<Miraculous> miraculous;
    private final Consumer<? super TransformationHistoryData.@Nullable TransformationEntry> consumer;
    private @Nullable TransformationHistoryData.TransformationEntry initialValue;

    public PowerTargetSelectionButton(
            int x,
            int y,
            int width,
            int height,
            Screen parentScreen,
            boolean allowNone,
            @Nullable Holder<Miraculous> miraculous,
            @Nullable TransformationHistoryData.TransformationEntry initialValue,
            Consumer<? super TransformationHistoryData.@Nullable TransformationEntry> consumer) {
        super(x, y, width, height, getDisplayLabel(initialValue), b -> {}, DEFAULT_NARRATION);
        this.parentScreen = parentScreen;
        this.allowNone = allowNone;
        this.miraculous = miraculous;
        this.initialValue = initialValue;
        this.consumer = consumer;
    }

    public static Builder builder(Screen parentScreen, Consumer<? super TransformationHistoryData.@Nullable TransformationEntry> consumer) {
        return new Builder(parentScreen, consumer);
    }

    @Override
    public void onPress() {
        // Opens the Identity Type Selection screen (Player vs Transformation entry)
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
                    } else {
                        Minecraft.getInstance().setScreen(new TransformationEntriesLoadingScreen(
                                parentScreen,
                                entry -> {
                                    consumer.accept(entry);
                                    Minecraft.getInstance().setScreen(parentScreen);
                                },
                                initialValue,
                                selectedMiraculous, this));
                        TommyLibServices.NETWORK.sendToServer(ServerboundRequestTransformationHistoryPayload.INSTANCE);
                    }
                }));
    }

    public @Nullable TransformationHistoryData.TransformationEntry getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(@Nullable TransformationHistoryData.TransformationEntry initialValue) {
        this.initialValue = initialValue;
        setMessage(getDisplayLabel(initialValue));
    }

    public static Component getDisplayLabel(@Nullable TransformationHistoryData.TransformationEntry transformationEntry) {
        if (transformationEntry == null) {
            return MineraculousClientUtils.GUI_CHOOSE;
        }

        return transformationEntry.getName();
    }

    public static class Builder {
        private final Screen parentScreen;
        private final Consumer<? super TransformationHistoryData.@Nullable TransformationEntry> onChanged;
        private boolean allowNone = false;
        private @Nullable Holder<Miraculous> miraculous = null;
        private @Nullable TransformationHistoryData.TransformationEntry initialValue = null;
        private int x = 0;
        private int y = 0;
        private int width = 150;
        private int height = 20;

        public Builder(Screen parentScreen, Consumer<? super TransformationHistoryData.@Nullable TransformationEntry> onChanged) {
            this.parentScreen = parentScreen;
            this.onChanged = onChanged;
        }

        public Builder allowNone(boolean allowNone) {
            this.allowNone = allowNone;
            return this;
        }

        public Builder miraculous(@Nullable Holder<Miraculous> miraculous) {
            this.miraculous = miraculous;
            return this;
        }

        public Builder initialValue(@Nullable TransformationHistoryData.TransformationEntry initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public PowerTargetSelectionButton build() {
            return new PowerTargetSelectionButton(x, y, width, height, parentScreen, allowNone, miraculous, initialValue, onChanged);
        }
    }
}
