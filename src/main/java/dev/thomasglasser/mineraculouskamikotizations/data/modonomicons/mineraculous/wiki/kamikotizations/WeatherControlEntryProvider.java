package dev.thomasglasser.mineraculouskamikotizations.data.modonomicons.mineraculous.wiki.kamikotizations;

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase;
import com.klikli_dev.modonomicon.api.datagen.IndexModeEntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookImagePageModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import dev.thomasglasser.mineraculouskamikotizations.data.modonomicons.mineraculous.wiki.AddToWikiBookSubProvider;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;

public class WeatherControlEntryProvider extends IndexModeEntryProvider {
    public static final String ID = "weather_control";

    public WeatherControlEntryProvider(CategoryProviderBase parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        page("overview", () -> BookImagePageModel.create()
                .withImages(AddToWikiBookSubProvider.wikiTexture("kamikotizations/weather_control/overview"))
                .withTitle(context().pageTitle())
                .withText(context().pageText()));

        pageTitle("Overview");
        pageText("""
                The Weather Control kamikotization grants the receiver the power over weather and storms.
                Its tool is the Weather Control Parasol.
                This kamikotization can be applied to any umbrella by default, including those from other mods (as long as they are in the 'c:tools/umbrella' tag).
                """);

        page("tool", () -> BookTextPageModel.create()
                .withTitle(context().pageTitle())
                .withText(context().pageText()));

        pageTitle("Tool");
        pageText("""
                The Weather Control Parasol has four tool modes:
                - Ice
                - Lightning
                - Weather
                - Wind

                When in any equipment slot, the holder is granted creative flight and reduced fall damage. When open and actively used, the holder will fall slowly.
                """);

        page("ice", () -> BookTextPageModel.create()
                .withTitle(context().pageTitle())
                .withText(context().pageText()));

        pageTitle("Ice");
        pageText("""
                The Ice mode will fire Ice Charges that freeze entities and blocks that they hit.
                When frozen, entities will be captured in a prison of ice and will freeze to death.
                """);

        page("lightning", () -> BookTextPageModel.create()
                .withTitle(context().pageTitle())
                .withText(context().pageText()));

        pageTitle("Lightning");
        pageText("""
                The Lightning mode will summon Lightning Bolts at the targeted entity or block, damaging them and lighting fires in the process.
                """);

        page("weather", () -> BookTextPageModel.create()
                .withTitle(context().pageTitle())
                .withText(context().pageText()));

        pageTitle("Weather");
        pageText("""
                The Weather mode will cycle the weather through rain, thunder, and clear.
                """);

        page("wind", () -> BookTextPageModel.create()
                .withTitle(context().pageTitle())
                .withText(context().pageText()));

        pageTitle("Wind");
        pageText("""
                The Wind mode will fire Wind Charges that will push entities and blocks that they hit.
                """);
    }

    @Override
    protected String entryName() {
        return "Weather Control";
    }

    @Override
    protected String entryDescription() {
        return "Powers to control the weather";
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL);
    }

    @Override
    protected String entryId() {
        return ID;
    }
}
