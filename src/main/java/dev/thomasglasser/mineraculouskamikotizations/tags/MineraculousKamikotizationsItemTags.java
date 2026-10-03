package dev.thomasglasser.mineraculouskamikotizations.tags;

import dev.thomasglasser.mineraculous.api.tags.MineraculousTagUtils;
import dev.thomasglasser.mineraculous.api.world.kamikotization.Kamikotization;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization.MineraculousKamikotizationsKamikotizations;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MineraculousKamikotizationsItemTags {
    // Kamikotizables
    public static final TagKey<Item> WEATHER_CONTROL_KAMIKOTIZABLE = kamikotizable(MineraculousKamikotizationsKamikotizations.WEATHER_CONTROL);

    // Mod Specific
    public static final TagKey<Item> PARASOLS = create("parasols");

    // Common
    public static final TagKey<Item> UMBRELLA_TOOLS = createC("tools/umbrella");

    public static TagKey<Item> kamikotizable(ResourceKey<Kamikotization> kamikotization) {
        return TagKey.create(Registries.ITEM, kamikotization.location().withPrefix("kamikotizable/"));
    }

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, MineraculousKamikotizations.modLoc(name));
    }

    private static TagKey<Item> createC(String name) {
        return MineraculousTagUtils.cTag(Registries.ITEM, name);
    }
}
