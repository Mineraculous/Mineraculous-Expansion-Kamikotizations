package dev.thomasglasser.mineraculouskamikotizations.data;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.data.MineraculousDataGenerationUtils;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.data.advancements.MineraculousKamikotizationsAdvancementProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.datamaps.MineraculousKamikotizationsDataMapProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.lang.MineraculousKamikotizationsEnUsLanguageProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.look.MineraculousKamikotizationsLookProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.loot.MineraculousKamikotizationsLootTables;
import dev.thomasglasser.mineraculouskamikotizations.data.models.MineraculousKamikotizationsItemModelProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.modonomicons.MineraculousKamikotizationsBookProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.recipes.MineraculousKamikotizationsRecipeProvider;
import dev.thomasglasser.mineraculouskamikotizations.data.tags.MineraculousKamikotizationsItemTagsProvider;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization.MineraculousKamikotizationsKamikotizations;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.RegistrySetBuilder;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class MineraculousKamikotizationsDataGenerators {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(MineraculousRegistries.KAMIKOTIZATION, MineraculousKamikotizationsKamikotizations::bootstrap);

    public static void onGatherData(GatherDataEvent event) {
        // Server
        event.createDatapackRegistryObjects(BUILDER);
        MineraculousDataGenerationUtils.createRegistryDumpReport(event, MineraculousKamikotizations.MOD_ID);
        MineraculousDataGenerationUtils.createProvider(event, (packOutput, lookupProvider, existingFileHelper) -> new MineraculousKamikotizationsItemTagsProvider(packOutput, lookupProvider, CompletableFuture.completedFuture(null), existingFileHelper));
        event.createProvider(MineraculousKamikotizationsDataMapProvider::new);
        event.createProvider(MineraculousKamikotizationsLootTables::new);
        event.createProvider(MineraculousKamikotizationsRecipeProvider::new);

        // Common
        MineraculousDataGenerationUtils.createLangDependent(event, MineraculousKamikotizationsEnUsLanguageProvider::new, MineraculousKamikotizationsAdvancementProvider::new, MineraculousKamikotizationsBookProvider::new);

        // Client
        MineraculousDataGenerationUtils.createProvider(event, MineraculousKamikotizationsItemModelProvider::new);
        event.createProvider(MineraculousKamikotizationsLookProvider::new);
    }
}
