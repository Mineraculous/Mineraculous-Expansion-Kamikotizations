package dev.thomasglasser.mineraculouskamikotizations.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import dev.thomasglasser.mineraculous.api.client.gui.IconRenderers;
import dev.thomasglasser.mineraculous.api.client.gui.screens.HolderSelectionScreen;
import dev.thomasglasser.mineraculous.api.client.gui.screens.SelectionScreen;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.IdentityTypeSelectionScreen;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.TransformationEntriesLoadingScreen;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.TransformationEntriesSyncListener;
import dev.thomasglasser.mineraculouskamikotizations.network.ServerboundRequestTransformationHistoryPayload;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.targetvisual.VisualTarget;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class MineraculousKamikotizationsClientUtils {
    public static final Component PLAYER_SELECTION_SCREEN_TITLE = MineraculousClientUtils.translatableGui("player_selection", "title");

    public static void triggerTransformationEntriesSyncListener(ImmutableSet<TransformationHistoryData.TransformationEntry> transformationEntries) {
        if (Minecraft.getInstance().screen instanceof TransformationEntriesSyncListener listener)
            listener.onTransformationEntriesSynced(transformationEntries);
    }

    public static Button createVisualTargetButton(Screen parentScreen, boolean allowNone, @Nullable Holder<Miraculous> miraculous, @Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry> currentValue, int width, int height, Consumer<? super @Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry>> onChanged) {
        return Button.builder(getVisualTargetLabel(currentValue), button -> parentScreen.getMinecraft().setScreen(
                new IdentityTypeSelectionScreen(parentScreen, allowNone, miraculous, currentValue, selected -> {
                    button.setMessage(getVisualTargetLabel(selected));
                    onChanged.accept(selected);
                })))
                .bounds(0, 0, width, height)
                .build(builder -> new Button(builder) {
                    @Override
                    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                        if (currentValue != null) {
                            currentValue.map(
                                    target -> {
                                        GameProfile profile = getGameProfile(target.uuid());
                                        int iconX = this.getX() + 4;
                                        int iconY = this.getY() + (this.height - 16) / 2;
                                        return IconRenderers.PLAYER_FACE.render(guiGraphics, profile, iconX, iconY);
                                    },
                                    entry -> false);
                        }
                    }
                });
    }

    public static Component getVisualTargetLabel(@Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry> value) {
        return value != null ? value.map(target -> Component.literal(getPlayerName(target.uuid())), TransformationHistoryData.TransformationEntry::getName) : MineraculousClientUtils.GUI_CHOOSE;
    }

    private static GameProfile getGameProfile(UUID uuid) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() != null) {
            PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(uuid);
            if (playerInfo != null) {
                return playerInfo.getProfile();
            }
        }
        return new GameProfile(uuid, "");
    }

    private static String getPlayerName(UUID uuid) {
        GameProfile profile = getGameProfile(uuid);
        if (profile.getName() != null && !profile.getName().isEmpty()) {
            return profile.getName();
        }
        return "Player (" + uuid.toString().substring(0, 8) + ")";
    }

    public static Button createPowerTargetButton(Screen parent, boolean allowNone, @Nullable TransformationHistoryData.TransformationEntry currentValue, int width, int height, Consumer<? super TransformationHistoryData.@Nullable TransformationEntry> onChanged) {
        return Button.builder(currentValue != null ? currentValue.getName() : MineraculousClientUtils.GUI_CHOOSE, button -> parent.getMinecraft().setScreen(new HolderSelectionScreen<>(
                parent,
                allowNone,
                currentValue != null ? currentValue.miraculous() : null,
                MineraculousRegistries.MIRACULOUS,
                null,
                IconRenderers.MIRACULOUS,
                selectedMiraculous -> {
                    if (selectedMiraculous == null) {
                        if (allowNone) {
                            onChanged.accept(null);
                        }
                        parent.getMinecraft().setScreen(parent);
                    } else {
                        parent.getMinecraft().setScreen(new TransformationEntriesLoadingScreen(
                                parent,
                                entry -> {
                                    onChanged.accept(entry);
                                    parent.getMinecraft().setScreen(parent);
                                },
                                currentValue,
                                selectedMiraculous,
                                button));
                        TommyLibServices.NETWORK.sendToServer(ServerboundRequestTransformationHistoryPayload.INSTANCE);
                    }
                }))).bounds(0, 0, width, height).build();
    }

    public static void openMiraculousSelection(Screen parentScreen, boolean allowNone, @Nullable Holder<Miraculous> miraculous, Consumer<@Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry>> consumer, Consumer<Holder<Miraculous>> onMiraculousSelected) {
        parentScreen.getMinecraft().setScreen(new HolderSelectionScreen<>(parentScreen, allowNone, miraculous, MineraculousRegistries.MIRACULOUS, null, IconRenderers.MIRACULOUS, selectedMiraculous -> {
            if (selectedMiraculous == null) {
                if (allowNone) {
                    consumer.accept(null);
                }
                parentScreen.getMinecraft().setScreen(parentScreen);
                return;
            }
            onMiraculousSelected.accept(selectedMiraculous);
        }));
    }

    public static void openPlayerMiraculousSelection(Screen parentScreen, boolean allowNone, @Nullable Holder<Miraculous> miraculous, @Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry> initialValue, Consumer<@Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry>> consumer) {
        openMiraculousSelection(parentScreen, allowNone, miraculous, consumer, selectedMiraculous -> openPlayerSelection(parentScreen, false, initialValue != null ? initialValue.left().map(VisualTarget::uuid).orElse(null) : null, selectedUuid -> {
            consumer.accept(selectedUuid != null ? Either.left(new VisualTarget(selectedMiraculous, selectedUuid)) : null);
            parentScreen.getMinecraft().setScreen(parentScreen);
        }));
    }

    public static void openTransformationEntryMiraculousSelection(Screen parentScreen, boolean allowNone, @Nullable Holder<Miraculous> miraculous, @Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry> initialValue, Button entryButton, Consumer<@Nullable Either<VisualTarget, TransformationHistoryData.TransformationEntry>> consumer) {
        openMiraculousSelection(parentScreen, allowNone, miraculous, consumer, selectedMiraculous -> {
            TransformationHistoryData.TransformationEntry initialEntry = initialValue != null ? initialValue.right().orElse(null) : null;
            parentScreen.getMinecraft().setScreen(new TransformationEntriesLoadingScreen(parentScreen, entry -> {
                consumer.accept(entry != null ? Either.right(entry) : null);
                parentScreen.getMinecraft().setScreen(parentScreen);
            }, initialEntry, selectedMiraculous, entryButton));
            TommyLibServices.NETWORK.sendToServer(ServerboundRequestTransformationHistoryPayload.INSTANCE);
        });
    }

    public static void openPlayerSelection(Screen parentScreen, boolean allowNone, @Nullable UUID current, Consumer<UUID> onSelected) {
        parentScreen.getMinecraft().setScreen(new SelectionScreen<>(PLAYER_SELECTION_SCREEN_TITLE, parentScreen, allowNone, current, getOnlinePlayerUuids(), uuid -> {
            GameProfile profile = getGameProfile(uuid);
            if (profile.getName() != null && !profile.getName().isEmpty()) {
                return Component.literal(profile.getName());
            }
            return Component.literal("Player (" + uuid.toString().substring(0, 8) + ")");
        }, IconRenderers.PLAYER_FACE.map(MineraculousKamikotizationsClientUtils::getGameProfile), onSelected));
    }

    private static ImmutableList<UUID> getOnlinePlayerUuids() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getConnection() == null) {
            return ImmutableList.of();
        }
        Collection<PlayerInfo> onlinePlayers = minecraft.getConnection().getOnlinePlayers();
        ImmutableList.Builder<UUID> builder = ImmutableList.builderWithExpectedSize(onlinePlayers.size());
        for (PlayerInfo info : onlinePlayers) {
            builder.add(info.getProfile().getId());
        }
        return builder.build();
    }
}
