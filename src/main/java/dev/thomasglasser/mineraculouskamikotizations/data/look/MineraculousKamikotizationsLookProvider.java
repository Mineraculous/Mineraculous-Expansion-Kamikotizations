package dev.thomasglasser.mineraculouskamikotizations.data.look;

import dev.thomasglasser.mineraculous.impl.data.looks.MineraculousLookProvider;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization.MineraculousKamikotizationsKamikotizations;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class MineraculousKamikotizationsLookProvider extends MineraculousLookProvider {
    public MineraculousKamikotizationsLookProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, MineraculousKamikotizations.MOD_ID, lookupProvider);
    }

    @Override
    protected void registerLooks(HolderLookup.Provider provider) {
        kamikotizationLook(MineraculousKamikotizationsKamikotizations.WEATHER_CONTROL);
        kamikotizationLookNoAnims(MineraculousKamikotizationsKamikotizations.BUBBLE_CAPTURE);
    }
}
