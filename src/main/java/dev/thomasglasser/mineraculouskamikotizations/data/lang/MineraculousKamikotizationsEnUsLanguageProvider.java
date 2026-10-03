package dev.thomasglasser.mineraculouskamikotizations.data.lang;

import dev.thomasglasser.mineraculous.api.data.lang.MineraculousEnUsLanguageProvider;
import dev.thomasglasser.mineraculous.api.world.kamikotization.condition.ItemCondition;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.IdentityTypeSelectionScreen;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.LoadingScreen;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.TransformationEntriesLoadingScreen;
import dev.thomasglasser.mineraculouskamikotizations.tags.MineraculousKamikotizationsItemTags;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.MineraculousKamikotizationsAbilities;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.kamikotization.MineraculousKamikotizationsKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsCreativeModeTabs;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import dev.thomasglasser.mineraculouskamikotizations.world.item.WeatherControlParasolItem;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;

public class MineraculousKamikotizationsEnUsLanguageProvider extends MineraculousEnUsLanguageProvider {
    public MineraculousKamikotizationsEnUsLanguageProvider(PackOutput output) {
        super(output, MineraculousKamikotizations.MOD_ID);
    }

    @Override
    protected void addTranslations() {
        addItems();
        addEntities();
        addTabs();
        addKamikotizations();
        addCustomizationSettings();
        addAbilities();
        addGuis();
    }

    protected void addItems() {
        // Kamikotizables
        for (DyeColor color : DyeColor.values()) {
            add(MineraculousKamikotizationsItems.PARASOLS.get(color).get(), capitalize(color.getName()) + " Parasol");
        }

        // Kamikotization Tools
        add(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.get(), "Weather Control Parasol");
        addHotbarMenuOptions(WeatherControlParasolItem.Mode.values());
    }

    protected void addEntities() {
        add(MineraculousKamikotizationsEntityTypes.ICE_CHARGE.get(), "Ice Charge");
        add(MineraculousKamikotizationsEntityTypes.REVERTING_ICE_CHARGE.get(), "Ice Charge");
        add(MineraculousKamikotizationsEntityTypes.REVERTING_LIGHTNING_BOLT.get(), "Lightning Bolt");
        add(MineraculousKamikotizationsEntityTypes.REVERTING_WIND_CHARGE.get(), "Wind Charge");
    }

    protected void addTabs() {
        add(MineraculousKamikotizationsCreativeModeTabs.MINERACULOUS_KAMIKOTIZATIONS.get(), "Mineraculous Expansion: Kamikotizations");
    }

    protected void addKamikotizations() {
        add(MineraculousKamikotizationsKamikotizations.WEATHER_CONTROL, "Weather Control");
        add(ItemCondition.requiresMessage(MineraculousKamikotizationsItemTags.WEATHER_CONTROL_KAMIKOTIZABLE), "Requires an Umbrella");
        add(MineraculousKamikotizationsKamikotizations.MIRACULOUS_MIMICRY, "Miraculous Mimicry");
    }

    protected void addCustomizationSettings() {
        // Abilities
        addCapitalizedCustomizationSettings(MineraculousKamikotizationsKamikotizations.MIRACULOUS_MIMICRY, MineraculousKamikotizationsAbilities.MIMIC_ACTIVE_MIRACULOUS, "power_target", "timer");
        addCapitalizedCustomizationSettings(MineraculousKamikotizationsKamikotizations.MIRACULOUS_MIMICRY, MineraculousKamikotizationsAbilities.MIMIC_PASSIVE_MIRACULOUS, "power_target");
        addCapitalizedCustomizationSettings(MineraculousKamikotizationsKamikotizations.MIRACULOUS_MIMICRY, MineraculousKamikotizationsAbilities.MIMIC_VISUAL_MIRACULOUS, "visual_target", "looks", "copy_name_color", "copy_name");
    }

    protected void addAbilities() {
        add(MineraculousKamikotizationsAbilities.MIMIC_ACTIVE_MIRACULOUS, "Mimic Miraculous Active");
        add(MineraculousKamikotizationsAbilities.MIMIC_PASSIVE_MIRACULOUS, "Mimic Miraculous Passives");
        add(MineraculousKamikotizationsAbilities.MIMIC_VISUAL_MIRACULOUS, "Mimic Miraculous Visuals");
    }

    protected void addGuis() {
        // Loading Screen
        add(LoadingScreen.GUI_LOADING, "Loading...");
        add(LoadingScreen.NARRATOR_LOADING_DONE, "Done!");

        // Identity Type Selection Screen
        add(IdentityTypeSelectionScreen.IDENTITY_TYPE_SELECTION_SCREEN_TITLE, "Select Identity Type");
        add(IdentityTypeSelectionScreen.IDENTITY_TYPE_SELECTION_SCREEN_PLAYER, "Player");
        add(IdentityTypeSelectionScreen.IDENTITY_TYPE_SELECTION_SCREEN_TRANSFORMATION_ENTRY, "Transformation Entry");

        // Transformation Entries Screen
        add(TransformationEntriesLoadingScreen.TRANSFORMATION_ENTRY_SELECTION_SCREEN_TITLE, "Transformation Entry Selection");

        // Player Selection Screen
        add(MineraculousKamikotizationsClientUtils.PLAYER_SELECTION_SCREEN_TITLE, "Select Player");
    }
}
