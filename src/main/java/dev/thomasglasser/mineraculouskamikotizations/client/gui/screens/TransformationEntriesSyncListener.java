package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;

public interface TransformationEntriesSyncListener {
    void onTransformationEntrySync(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries);
}
