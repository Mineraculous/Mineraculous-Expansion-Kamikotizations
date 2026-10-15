package dev.thomasglasser.mineraculouskamikotizations.data.loot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.data.loot.MineraculousLootTableProvider;
import dev.thomasglasser.mineraculous.api.world.level.storage.loot.parameters.MineraculousLootContextParamSets;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class MineraculousKamikotizationsLootTables extends MineraculousLootTableProvider {
    public MineraculousKamikotizationsLootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, ImmutableSet.of(), ImmutableList.of(
                new SubProviderEntry(MineraculousKamikotizationsLuckyCharmLoot::new, MineraculousLootContextParamSets.LUCKY_CHARM)), lookupProvider);
    }
}
