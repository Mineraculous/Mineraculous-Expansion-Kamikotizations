package dev.thomasglasser.mineraculouskamikotizations;

import dev.thomasglasser.mineraculouskamikotizations.core.component.MineraculousKamikotizationsDataComponents;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.data.MineraculousKamikotizationsDataGenerators;
import dev.thomasglasser.mineraculouskamikotizations.event.MineraculousKamikotizationsKamikotizationEvents;
import dev.thomasglasser.mineraculouskamikotizations.event.MineraculousKamikotizationsMiraculousEvents;
import dev.thomasglasser.mineraculouskamikotizations.network.MineraculousKamikotizationsPayloads;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MineraculousKamikotizationsAbilityActions;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityEvents;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsCreativeModeTabs;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MineraculousKamikotizations.MOD_ID)
public class MineraculousKamikotizations {
    public static final String MOD_ID = "mineraculouskamikotizations";
    public static final String MOD_NAME = "Mineraculous Expansion: Kamikotizations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public MineraculousKamikotizations(IEventBus modBus) {
        MineraculousKamikotizationsAbilityActions.init();
        MineraculousKamikotizationsAbilityStates.init();
        MineraculousKamikotizationsCustomizationSettingTypes.init();
        MineraculousKamikotizationsEntityTypes.init();
        MineraculousKamikotizationsItems.init();
        MineraculousKamikotizationsCreativeModeTabs.init();
        MineraculousKamikotizationsDataComponents.init();

        modBus.addListener(MineraculousKamikotizationsPayloads::onRegisterPayloadHandlers);
        modBus.addListener(MineraculousKamikotizationsDataGenerators::onGatherData);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOW, MineraculousKamikotizationsKamikotizationEvents::onFinishKamikotizationTransform);
        NeoForge.EVENT_BUS.addListener(MineraculousKamikotizationsMiraculousEvents::onFinishMiraculousTransform);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, MineraculousKamikotizationsEntityEvents::onPlayerNameFormat);
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
