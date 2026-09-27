package dev.thomasglasser.mineraculouskamikotizations.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculouskamikotizations.client.MineraculousKamikotizationsClientUtils;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class PlayerSelectionScreen extends SelectionScreen<UUID> {
    public PlayerSelectionScreen(Screen parent, boolean allowNone, @Nullable UUID current, Consumer<UUID> onSelected) {
        super(MineraculousKamikotizationsClientUtils.PLAYER_SELECTION_SCREEN_TITLE, parent, allowNone, current, getOnlinePlayerUuids(), PlayerSelectionScreen::getPlayerName, IconRenderers.PLAYER_FACE.map(PlayerSelectionScreen::getGameProfile), onSelected);
    }

    private static ImmutableList<UUID> getOnlinePlayerUuids() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() == null) {
            return ImmutableList.of();
        }
        Collection<PlayerInfo> onlinePlayers = minecraft.getConnection().getOnlinePlayers();
        return ImmutableList.copyOf(onlinePlayers.stream().map(info -> info.getProfile().getId()).toList());
    }

    private static Component getPlayerName(UUID uuid) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() != null) {
            PlayerInfo info = minecraft.getConnection().getPlayerInfo(uuid);
            if (info != null && info.getProfile().getName() != null) {
                return Component.literal(info.getProfile().getName());
            }
        }
        return Component.literal(uuid.toString());
    }

    private static GameProfile getGameProfile(UUID uuid) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() != null) {
            PlayerInfo info = minecraft.getConnection().getPlayerInfo(uuid);
            if (info != null) {
                return info.getProfile();
            }
        }
        return new GameProfile(uuid, "");
    }
}
