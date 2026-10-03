package dev.thomasglasser.mineraculouskamikotizations.data.advancements;

import dev.thomasglasser.mineraculous.api.data.advancements.MineraculousAdvancementProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.advancements.packs.MineraculousKamikotizationsKamikotizationAdvancements;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MineraculousKamikotizationsAdvancementProvider extends MineraculousAdvancementProvider {
    public MineraculousKamikotizationsAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, BiConsumer<String, String> lang, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, ReferenceOpenHashSet.of(
                new MineraculousKamikotizationsKamikotizationAdvancements(lang)));
    }
}
