package dev.thomasglasser.mineraculouskamikotizations.client.gui.selection;

import dev.thomasglasser.mineraculous.api.client.gui.selection.HotbarMenuIcons;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import net.minecraft.resources.ResourceLocation;

public class MineraculousKamikotizationsHotbarMenuIcons {
    // TODO: Icon textures
    public static final ResourceLocation CAGE = icon("cage");
    public static final ResourceLocation CLOUD = icon("cloud");
    public static final ResourceLocation EXPLOSION = icon("explosion");
    public static final ResourceLocation ICE = icon("ice");
    public static final ResourceLocation LIGHTNING = icon("lightning");
    public static final ResourceLocation WIND = icon("wind");
    public static final ResourceLocation WINGS = icon("wings");

    private static ResourceLocation icon(String name) {
        return HotbarMenuIcons.icon(MineraculousKamikotizations.modLoc(name));
    }
}
