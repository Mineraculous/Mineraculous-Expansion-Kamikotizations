package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;

public interface TransformationEntriesSyncListener {
    void onTransformationEntriesSynced(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries);
}
