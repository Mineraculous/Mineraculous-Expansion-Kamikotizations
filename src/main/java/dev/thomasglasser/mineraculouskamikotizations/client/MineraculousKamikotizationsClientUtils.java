package dev.thomasglasser.mineraculouskamikotizations.client;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.TransformationEntriesSyncListener;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class MineraculousKamikotizationsClientUtils {
    public static final Component GUI_LOADING = Component.translatable("gui.loading");
    public static final Component TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE = MineraculousClientUtils.translatableGui("transformation_entry_selection", "title");

    public static void triggerTransformationEntriesSyncListener(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries) {
        if (Minecraft.getInstance().screen instanceof TransformationEntriesSyncListener tracker)
            tracker.onTransformationEntriesSync(transformationEntries);
            tracker.onTransformationEntriesSync(transformationEntries);
        }
    }
}
