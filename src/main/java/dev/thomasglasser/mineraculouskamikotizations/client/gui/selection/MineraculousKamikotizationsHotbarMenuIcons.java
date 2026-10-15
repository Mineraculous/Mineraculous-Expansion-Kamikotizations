package dev.thomasglasser.mineraculouskamikotizations.client.gui.selection;

import dev.thomasglasser.mineraculous.api.client.gui.selection.HotbarMenuIcons;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import net.minecraft.resources.ResourceLocation;

public class MineraculousKamikotizationsHotbarMenuIcons {
    public static final ResourceLocation ICE = create("ice");
    public static final ResourceLocation LIGHTNING = create("lightning");
    public static final ResourceLocation CLOUD = create("cloud");
    public static final ResourceLocation WIND = create("wind");

    private static ResourceLocation create(String name) {
        return HotbarMenuIcons.icon(MineraculousKamikotizations.modLoc(name));
    }
}
