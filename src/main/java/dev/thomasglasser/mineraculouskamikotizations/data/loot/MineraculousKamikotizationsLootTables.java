package dev.thomasglasser.mineraculouskamikotizations.data.loot;

import dev.thomasglasser.mineraculous.api.data.loot.MineraculousLootTableProvider;
import dev.thomasglasser.mineraculous.api.world.level.storage.loot.parameters.MineraculousLootContextParamSets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class MineraculousKamikotizationsLootTables extends MineraculousLootTableProvider {
    public MineraculousKamikotizationsLootTables(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pOutput, Set.of(), List.of(
                new SubProviderEntry(MineraculousKamikotizationsLuckyCharmLoot::new, MineraculousLootContextParamSets.LUCKY_CHARM)), lookupProvider);
    }
}
