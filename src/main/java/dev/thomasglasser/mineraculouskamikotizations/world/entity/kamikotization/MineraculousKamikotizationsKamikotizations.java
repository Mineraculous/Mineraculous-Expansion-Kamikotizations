package dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.kamikotization.Kamikotization;
import dev.thomasglasser.mineraculous.api.world.kamikotization.condition.ItemCondition;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsItemTags;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class MineraculousKamikotizationsKamikotizations {
    public static final ResourceKey<Kamikotization> WEATHER_CONTROL = create("weather_control");

    private static ResourceKey<Kamikotization> create(String name) {
        return ResourceKey.create(MineraculousRegistries.KAMIKOTIZATION, MineraculousKamikotizations.modLoc(name));
    }

    public static void bootstrap(BootstrapContext<Kamikotization> context) {
        context.register(WEATHER_CONTROL, Kamikotization.builder(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.toStack())
                .condition(new ItemCondition(MineraculousKamikotizationsItemTags.WEATHER_CONTROL_KAMIKOTIZABLE, predicate -> predicate.withCount(MinMaxBounds.Ints.exactly(1))))
                .build());
    }
}
