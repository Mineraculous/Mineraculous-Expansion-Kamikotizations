package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableList;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderer;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculous.api.core.component.MineraculousDataComponents;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData.TransformationEntry;
import java.util.Collection;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/// Selects a player UUID based on their historical transformation entries.
public class PlayerSelectionScreen extends SelectionScreen<TransformationEntry> {
    public static final String ID = "player_selection";
    public static final Component TITLE = MineraculousClientUtils.translatableGui(ID, "title");

    public PlayerSelectionScreen(@Nullable Screen parent, boolean allowNone, @Nullable TransformationEntry initialValue, Collection<TransformationEntry> entries, Consumer<@Nullable TransformationEntry> onApply) {
        super(TITLE, parent, allowNone, initialValue, ImmutableList.copyOf(entries),
                PlayerSelectionScreen::getEntryDisplayName,
                createMiraculousIconRenderer(),
                onApply);
    }

    public static Component getEntryDisplayName(TransformationEntry entry) {
        Component customName = getCustomName(entry);
        if (customName != null) {
            return customName.copy();
        }
        return Miraculous.createMiraculousStack(entry.miraculous()).getHoverName();
    }

    private static @Nullable Component getCustomName(TransformationEntry entry) {
        return entry.customizationData()
                .flatMap(CustomizationData::nameSetting)
                .map(nameSetting -> {
                    CustomizationData customizationData = entry.customizationData().get();
                    String actualName = customizationData.settings().get(nameSetting);
                    if (actualName != null && !actualName.isEmpty()) {
                        return Component.literal(actualName).withStyle(style -> style.withColor(entry.miraculous().value().color()));
                    }
                    return null;
                }).orElse(null);
    }

    private static IconRenderer<TransformationEntry> createMiraculousIconRenderer() {
        return (guiGraphics, transformationEntry, x, y) -> {
            ItemStack miraculousStack = Miraculous.createMiraculousStack(transformationEntry.miraculous());
            transformationEntry.customizationData().ifPresent(data -> miraculousStack.set(MineraculousDataComponents.CUSTOMIZATION_DATA, data));
            return IconRenderers.ITEM_STACK.render(guiGraphics, miraculousStack, x, y);
        };
    }
}
