package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.GameProfileCache;
import org.jetbrains.annotations.Nullable;

public class PlayerSelectionList extends ContainerObjectSelectionList<PlayerSelectionList.Entry> {
    private final List<Entry> profileEntries = new ObjectArrayList<>();
    private final Consumer<GameProfile> onProfileSelected;
    private @Nullable String filterQuery;

    public PlayerSelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight, Consumer<GameProfile> onProfileSelected) {
        super(minecraft, width, height, y, itemHeight);
        this.onProfileSelected = onProfileSelected;
    }

    @Override
    protected void renderListBackground(GuiGraphics guiGraphics) {}

    @Override
    protected void renderListSeparators(GuiGraphics guiGraphics) {}

    @Override
    protected void enableScissor(GuiGraphics guiGraphics) {
        guiGraphics.enableScissor(this.getX(), this.getY() + 4, this.getRight(), this.getBottom());
    }

    public void updatePlayerList(double currentScrollAmount) {
        Set<Entry> updatedEntries = new ObjectOpenHashSet<>();

        List<GameProfile> availableProfiles = this.collectKnownGameProfiles();
        for (GameProfile profile : availableProfiles) {
            Supplier<PlayerSkin> skinSupplier = () -> {
                if (this.minecraft.getConnection() != null) {
                    PlayerInfo onlinePlayerInfo = this.minecraft.getConnection().getPlayerInfo(profile.getId());
                    if (onlinePlayerInfo != null) {
                        return onlinePlayerInfo.getSkin();
                    }
                }
                return this.minecraft.getSkinManager().getInsecureSkin(profile);
            };

            updatedEntries.add(new Entry(profile, skinSupplier, this.onProfileSelected));
        }

        this.applyFiltersAndScroll(updatedEntries, currentScrollAmount);
    }

    private List<GameProfile> collectKnownGameProfiles() {
        List<GameProfile> gatheredProfiles = new ObjectArrayList<>();

        if (this.minecraft.getSingleplayerServer() != null) {
            try {
                Field cacheMapField = GameProfileCache.class.getDeclaredField("profilesByUUID");
                cacheMapField.setAccessible(true);
                Map<?, ?> profilesByUuidMap = (Map<?, ?>) cacheMapField.get(this.minecraft.getSingleplayerServer().getProfileCache());
                for (Object profileEntry : profilesByUuidMap.values()) {
                    Method extractProfileMethod = profileEntry.getClass().getDeclaredMethod("getProfile");
                    extractProfileMethod.setAccessible(true);
                    GameProfile profile = (GameProfile) extractProfileMethod.invoke(profileEntry);
                    gatheredProfiles.add(profile);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (this.minecraft.getConnection() != null) {
            for (PlayerInfo onlinePlayerInfo : this.minecraft.getConnection().getOnlinePlayers()) {
                gatheredProfiles.add(onlinePlayerInfo.getProfile());
            }
        }

        return gatheredProfiles;
    }

    private void applyFiltersAndScroll(Collection<Entry> newEntries, double targetScrollAmount) {
        this.profileEntries.clear();
        this.profileEntries.addAll(newEntries);
        this.sortProfileEntries();
        this.filterProfileEntries();
        this.replaceEntries(this.profileEntries);
        this.setScrollAmount(targetScrollAmount);
    }

    private void sortProfileEntries() {
        this.profileEntries.sort(Comparator.<Entry, Integer>comparing(entry -> {
            if (this.minecraft.isLocalPlayer(entry.getPlayerUuid())) {
                return 0;
            } else if (entry.getPlayerUuid().version() == 2) {
                return 4;
            } else {
                return 3;
            }
        }).thenComparing(entry -> {
            if (!entry.getPlayerName().isBlank()) {
                int firstCodePoint = entry.getPlayerName().codePointAt(0);
                if (firstCodePoint == 95 || (firstCodePoint >= 97 && firstCodePoint <= 122) || (firstCodePoint >= 65 && firstCodePoint <= 90) || (firstCodePoint >= 48 && firstCodePoint <= 57)) {
                    return 0;
                }
            }
            return 1;
        }).thenComparing(Entry::getPlayerName, String::compareToIgnoreCase));
    }

    private void filterProfileEntries() {
        if (this.filterQuery != null && !this.filterQuery.isEmpty()) {
            this.profileEntries.removeIf(entry -> !entry.getPlayerName().toLowerCase().contains(this.filterQuery));
            this.replaceEntries(this.profileEntries);
        }
    }

    public void setFilter(String filterQuery) {
        this.filterQuery = filterQuery;
        this.updatePlayerList(this.getScrollAmount());
    }

    public boolean isEmpty() {
        return this.profileEntries.isEmpty();
    }

    public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private final GameProfile gameProfile;
        private final Supplier<PlayerSkin> skinSupplier;
        private final Button selectionButton;

        public Entry(GameProfile gameProfile, Supplier<PlayerSkin> skinSupplier, Consumer<GameProfile> onSelectionConfirmed) {
            this.gameProfile = gameProfile;
            this.skinSupplier = skinSupplier;
            this.selectionButton = Button.builder(Component.literal(gameProfile.getName()), button -> onSelectionConfirmed.accept(gameProfile))
                    .bounds(0, 0, 150, 20)
                    .build();
        }

        public UUID getPlayerUuid() {
            return this.gameProfile.getId();
        }

        public String getPlayerName() {
            return this.gameProfile.getName();
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            int headXPosition = left + 4;
            int headYPosition = top + (height - 24) / 2;

            PlayerSkin playerSkin = this.skinSupplier.get();
            PlayerFaceRenderer.draw(guiGraphics, playerSkin.texture(), headXPosition, headYPosition, 24);

            this.selectionButton.setX(left + 34);
            this.selectionButton.setY(top + (height - 20) / 2);
            this.selectionButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(this.selectionButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(this.selectionButton);
        }
    }
}
