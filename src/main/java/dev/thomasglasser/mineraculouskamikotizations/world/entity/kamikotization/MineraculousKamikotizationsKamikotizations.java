package dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization;

import com.google.common.collect.ImmutableList;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityInstance;
import dev.thomasglasser.mineraculous.api.world.kamikotization.Kamikotization;
import dev.thomasglasser.mineraculous.api.world.kamikotization.condition.ItemCondition;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsItemTags;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.MineraculousKamikotizationsAbilities;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class MineraculousKamikotizationsKamikotizations {
    public static final ResourceKey<Kamikotization> WEATHER_CONTROL = create("weather_control");
    public static final ResourceKey<Kamikotization> MIRACULOUS_MIMICRY = create("miraculous_mimicry");

    public static void bootstrap(BootstrapContext<Kamikotization> context) {
        HolderGetter<Ability> abilities = context.lookup(MineraculousRegistries.ABILITY);

        context.register(WEATHER_CONTROL, Kamikotization.builder(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.toStack())
                .condition(new ItemCondition(MineraculousKamikotizationsItemTags.WEATHER_CONTROL_KAMIKOTIZABLE, predicate -> predicate.withCount(MinMaxBounds.Ints.exactly(1))))
                .build());

        context.register(MIRACULOUS_MIMICRY, Kamikotization.builder(AbilityInstance.of(abilities.getOrThrow(MineraculousKamikotizationsAbilities.MIMIC_ACTIVE_MIRACULOUS)))
                .passiveAbilities(ImmutableList.of(
                        AbilityInstance.of(abilities.getOrThrow(MineraculousKamikotizationsAbilities.MIMIC_PASSIVE_MIRACULOUS)),
                        AbilityInstance.of(abilities.getOrThrow(MineraculousKamikotizationsAbilities.MIMIC_VISUAL_MIRACULOUS))))
                .build());
    }

    private static ResourceKey<Kamikotization> create(String name) {
        return ResourceKey.create(MineraculousRegistries.KAMIKOTIZATION, MineraculousKamikotizations.modLoc(name));
    }
}
