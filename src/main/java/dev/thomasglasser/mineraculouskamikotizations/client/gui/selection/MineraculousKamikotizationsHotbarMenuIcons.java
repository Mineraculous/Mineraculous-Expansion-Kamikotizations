package dev.thomasglasser.mineraculouskamikotizations.client.gui.selection;

import dev.thomasglasser.mineraculous.api.client.gui.selection.HotbarMenuIcons;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import net.minecraft.resources.ResourceLocation;

public class MineraculousKamikotizationsHotbarMenuIcons {
    public static final ResourceLocation ICE = icon("ice");
    public static final ResourceLocation LIGHTNING = icon("lightning");
    public static final ResourceLocation CLOUD = icon("cloud");
    public static final ResourceLocation WIND = icon("wind");

    private static ResourceLocation icon(String name) {
        return HotbarMenuIcons.icon(MineraculousKamikotizations.modLoc(name));
    }
}
