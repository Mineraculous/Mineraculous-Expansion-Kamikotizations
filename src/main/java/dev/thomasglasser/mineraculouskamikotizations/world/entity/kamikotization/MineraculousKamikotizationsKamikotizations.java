package dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
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
    public static final ResourceKey<Kamikotization> BUBBLE_CAPTURE = create("bubble_capture");

    private static ResourceKey<Kamikotization> create(String name) {
        return ResourceKey.create(MineraculousRegistries.KAMIKOTIZATION, MineraculousKamikotizations.modLoc(name));
    }

    public static void bootstrap(BootstrapContext<Kamikotization> context) {
        context.register(WEATHER_CONTROL, new Kamikotization(
                ImmutableList.of(
                        new ItemCondition(MineraculousKamikotizationsItemTags.UMBRELLA_TOOLS, predicate -> predicate.withCount(MinMaxBounds.Ints.exactly(1)))),
                CustomizationSettings.EMPTY,
                Either.left(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.toStack()),
                ImmutableList.of()));
        context.register(BUBBLE_CAPTURE, new Kamikotization(
                ImmutableList.of(
                        new ItemCondition(MineraculousKamikotizationsItemTags.BUBBLE_WANDS, predicate -> predicate.withCount(MinMaxBounds.Ints.exactly(1)))),
                CustomizationSettings.EMPTY,
                Either.left(MineraculousKamikotizationsItems.BUBBLE_SWORD.toStack()),
                ImmutableList.of()));
    }
}
