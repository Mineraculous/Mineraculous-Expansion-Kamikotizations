package dev.thomasglasser.mineraculouskamikotizations.client.gui.components;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.IdentityTypeSelectionScreen;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

/// A button that displays the selected {@link UUID} or {@link TransformationHistoryData.TransformationEntry} and opens the identity type selection flow when clicked.
public class VisualTargetSelectionButton extends Button {
    private final Screen parentScreen;
    private final boolean allowNone;
    private final @Nullable Holder<Miraculous> miraculous;
    private final Consumer<? super @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> onChanged;
    private @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> value;

    public VisualTargetSelectionButton(
            int x,
            int y,
            int width,
            int height,
            Screen parentScreen,
            boolean allowNone,
            @Nullable Holder<Miraculous> miraculous,
            @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> initialValue,
            Consumer<? super @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> onChanged) {
        super(x, y, width, height, getDisplayLabel(initialValue), b -> {}, DEFAULT_NARRATION);
        this.parentScreen = parentScreen;
        this.allowNone = allowNone;
        this.miraculous = miraculous;
        this.value = initialValue;
        this.onChanged = onChanged;
    }

    public static Builder builder(Screen parentScreen, Consumer<? super @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> onChanged) {
        return new Builder(parentScreen, onChanged);
    }

    @Override
    public void onPress() {
        Minecraft.getInstance().setScreen(new IdentityTypeSelectionScreen(
                parentScreen,
                allowNone,
                miraculous,
                value,
                selected -> {
                    setValue(selected);
                    onChanged.accept(selected);
                }));
    }

    public @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> getValue() {
        return value;
    }

    public void setValue(@Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> value) {
        this.value = value;
        setMessage(getDisplayLabel(value));
    }

    public static Component getDisplayLabel(@Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> options) {
        if (options == null) {
            return MineraculousClientUtils.GUI_CHOOSE;
        }

        return options.map(
                pair -> Component.literal("Player: " + pair.getSecond().toString()),
                TransformationHistoryData.TransformationEntry::getName);
    }

    public static class Builder {
        private final Screen parentScreen;
        private final Consumer<? super @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> onChanged;
        private boolean allowNone = false;
        private @Nullable Holder<Miraculous> miraculous = null;
        private @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> initialValue = null;
        private int x = 0;
        private int y = 0;
        private int width = 150;
        private int height = 20;

        public Builder(Screen parentScreen, Consumer<? super @Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> onChanged) {
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

        public Builder initialValue(@Nullable Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> initialValue) {
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

        public VisualTargetSelectionButton build() {
            return new VisualTargetSelectionButton(x, y, width, height, parentScreen, allowNone, miraculous, initialValue, onChanged);
        }
    }
}
