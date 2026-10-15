package dev.thomasglasser.mineraculouskamikotizations.data.tags;

import dev.thomasglasser.mineraculous.api.data.tags.MineraculousItemTagsProvider;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsItemTags;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class MineraculousKamikotizationsItemTagsProvider extends MineraculousItemTagsProvider {
    public MineraculousKamikotizationsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable CompletableFuture<TagLookup<Block>> blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(output, future, blockTagsProvider, MineraculousKamikotizations.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Kamikotizables
        tag(MineraculousKamikotizationsItemTags.WEATHER_CONTROL_KAMIKOTIZABLE)
                .addTag(MineraculousKamikotizationsItemTags.UMBRELLA_TOOLS);

        // Mod Specific
        tag(MineraculousKamikotizationsItemTags.PARASOLS)
                .add(MineraculousKamikotizationsItems.PARASOLS.values().toArray(ItemLike[]::new));

        // Common
        tag(MineraculousKamikotizationsItemTags.UMBRELLA_TOOLS)
                .add(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL)
                .addTag(MineraculousKamikotizationsItemTags.PARASOLS);

        tag(Tags.Items.TOOLS)
                .addTag(MineraculousKamikotizationsItemTags.UMBRELLA_TOOLS);
    }
}
