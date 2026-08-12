package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.mojang.authlib.GameProfile;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import java.util.function.Consumer;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public class PlayerSelectionScreen extends Screen {
    static final String ID = "player_selection";
    public static final Component TITLE = MineraculousClientUtils.translatableGui(ID, "name");

    private final Screen previousScreen;
    private final HeaderAndFooterLayout screenLayout = new HeaderAndFooterLayout(this);
    private final Consumer<GameProfile> onProfileSelected;
    private PlayerSelectionList playerSelectionList;
    private @Nullable EditBox searchBoxWidget;
    private String lastSearchQuery = "";

    public PlayerSelectionScreen(Screen previousScreen, Consumer<GameProfile> onProfileSelected) {
        super(TITLE);
        this.previousScreen = previousScreen;
        this.onProfileSelected = onProfileSelected;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.previousScreen);
        }
    }

    private int calculateWindowHeight() {
        return Math.max(52, this.height - 128 - SocialInteractionsScreen.SEARCH_HEIGHT);
    }

    private int calculateListEndCoordinate() {
        return 80 + this.calculateWindowHeight() - SocialInteractionsScreen.BG_BORDER_SIZE;
    }

    private int calculateHorizontalMargin() {
        return (this.width - SocialInteractionsScreen.IMAGE_WIDTH) / 2;
    }

    @Override
    protected void init() {
        this.screenLayout.addTitleHeader(TITLE, this.font);
        this.playerSelectionList = new PlayerSelectionList(
                this.minecraft,
                this.width,
                this.calculateListEndCoordinate() - SocialInteractionsScreen.LIST_START,
                SocialInteractionsScreen.LIST_START,
                SocialInteractionsScreen.ITEM_HEIGHT,
                selectedGameProfile -> {
                    this.onProfileSelected.accept(selectedGameProfile);
                    this.onClose();
                });

        String initialSearchText = this.searchBoxWidget != null ? this.searchBoxWidget.getValue() : "";
        this.searchBoxWidget = new EditBox(this.font, this.calculateHorizontalMargin() + 28, 74, 200, 15, SocialInteractionsScreen.SEARCH_HINT) {
            @Override
            protected MutableComponent createNarrationMessage() {
                return !searchBoxWidget.getValue().isEmpty() && playerSelectionList.isEmpty()
                        ? super.createNarrationMessage().append(", ").append(SocialInteractionsScreen.EMPTY_SEARCH)
                        : super.createNarrationMessage();
            }
        };
        this.searchBoxWidget.setMaxLength(16);
        this.searchBoxWidget.setVisible(true);
        this.searchBoxWidget.setTextColor(-1);
        this.searchBoxWidget.setValue(initialSearchText);
        this.searchBoxWidget.setHint(SocialInteractionsScreen.SEARCH_HINT);
        this.searchBoxWidget.setResponder(this::handleSearchQueryChange);

        this.addRenderableWidget(this.searchBoxWidget);
        this.addWidget(this.playerSelectionList);
        this.playerSelectionList.updatePlayerList(this.playerSelectionList.getScrollAmount());

        GameNarrator narrator = this.minecraft.getNarrator();
        if (!this.searchBoxWidget.getValue().isEmpty() && this.playerSelectionList.isEmpty() && !this.searchBoxWidget.isFocused()) {
            narrator.sayNow(SocialInteractionsScreen.EMPTY_SEARCH);
        }

        this.screenLayout.addToFooter(Button.builder(CommonComponents.GUI_CANCEL, cancelButton -> this.onClose()).width(200).build());
        this.screenLayout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.screenLayout.arrangeElements();
        this.playerSelectionList.updateSizeAndPosition(this.width, this.calculateListEndCoordinate() - SocialInteractionsScreen.LIST_START, SocialInteractionsScreen.LIST_START);
        if (this.searchBoxWidget != null) {
            this.searchBoxWidget.setPosition(this.calculateHorizontalMargin() + 28, 74);
        }
    }

    @Override
    protected void setInitialFocus() {
        if (this.searchBoxWidget != null) {
            this.setInitialFocus(this.searchBoxWidget);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int backgroundLeftPosition = this.calculateHorizontalMargin() + 3;
        guiGraphics.blitSprite(SocialInteractionsScreen.BACKGROUND_SPRITE, backgroundLeftPosition, SocialInteractionsScreen.MARGIN_Y, SocialInteractionsScreen.BG_WIDTH, this.calculateWindowHeight() + SocialInteractionsScreen.SEARCH_HEIGHT);
        guiGraphics.blitSprite(SocialInteractionsScreen.SEARCH_SPRITE, backgroundLeftPosition + 10, 76, 12, 12);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (!this.playerSelectionList.isEmpty()) {
            this.playerSelectionList.render(guiGraphics, mouseX, mouseY, partialTick);
        } else if (this.searchBoxWidget != null && !this.searchBoxWidget.getValue().isEmpty()) {
            guiGraphics.drawCenteredString(this.minecraft.font, SocialInteractionsScreen.EMPTY_SEARCH, this.width / 2, (SocialInteractionsScreen.SEARCH_START + this.calculateListEndCoordinate()) / 2, -1);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void handleSearchQueryChange(String updatedQuery) {
        String normalizedQuery = updatedQuery.toLowerCase();
        if (!normalizedQuery.equals(this.lastSearchQuery)) {
            this.playerSelectionList.setFilter(normalizedQuery);
            this.lastSearchQuery = normalizedQuery;
        }
    }
}
