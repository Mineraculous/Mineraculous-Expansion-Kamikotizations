package dev.thomasglasser.mineraculouskamikotizations.client;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = MineraculousKamikotizations.MOD_ID, dist = Dist.CLIENT)
public class MineraculousKamikotizationsClient {
    public MineraculousKamikotizationsClient(IEventBus modBus) {
        modBus.addListener(MineraculousKamikotizationsClientEvents::onFMLClientSetup);
        modBus.addListener(MineraculousKamikotizationsClientEvents::onBuildCreativeModeTabContents);
        modBus.addListener(MineraculousKamikotizationsClientEvents::onRegisterClientExtensions);
        modBus.addListener(MineraculousKamikotizationsClientEvents::onRegisterRenderers);
    }
}
