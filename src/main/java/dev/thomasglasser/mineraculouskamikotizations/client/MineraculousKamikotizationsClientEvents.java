package dev.thomasglasser.mineraculouskamikotizations.client;

import dev.thomasglasser.mineraculous.api.client.gui.components.CustomizationSettingWidgetRegistry;
import dev.thomasglasser.mineraculous.api.world.item.MineraculousItems;
import dev.thomasglasser.mineraculous.impl.client.MineraculousClientUtils;
import dev.thomasglasser.mineraculouskamikotizations.client.gui.screens.PlayerSelectionScreen;
import dev.thomasglasser.mineraculouskamikotizations.client.renderer.item.MineraculousKamikotizationsItemProperties;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import dev.thomasglasser.mineraculouskamikotizations.world.item.ParasolItem;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.WindChargeRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class MineraculousKamikotizationsClientEvents {
    // Init
    static void onFMLClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(MineraculousKamikotizationsItemProperties::init);
        event.enqueueWork(() -> {
            CustomizationSettingWidgetRegistry.register(MineraculousKamikotizationsCustomizationSettingTypes.PLAYER.get(), (property, parentScreen, font, widgetWidth, widgetHeight, currentUuidString, onValueChanged) -> {
                final String[] selectedPlayerUuidHolder = new String[] { currentUuidString };

                Component initialButtonText = resolvePlayerName(selectedPlayerUuidHolder[0]);

                return Button.builder(initialButtonText, selectionButton -> {
                    Minecraft.getInstance().setScreen(new PlayerSelectionScreen(
                            parentScreen,
                            selectedProfile -> {
                                if (selectedProfile != null) {
                                    String playerUuidString = selectedProfile.getId().toString();
                                    String playerDisplayName = selectedProfile.getName();

                                    selectedPlayerUuidHolder[0] = playerUuidString;
                                    onValueChanged.accept(playerUuidString);
                                    selectionButton.setMessage(Component.literal(playerDisplayName));
                                } else {
                                    selectedPlayerUuidHolder[0] = null;
                                    onValueChanged.accept(null);
                                    selectionButton.setMessage(MineraculousClientUtils.GUI_CHOOSE);
                                }
                                Minecraft.getInstance().setScreen(parentScreen);
                            }));
                }).size(widgetWidth, widgetHeight).build();
            });
        });
    }

    private static Component resolvePlayerName(@Nullable String uuidString) {
        if (uuidString == null || uuidString.isEmpty()) {
            return MineraculousClientUtils.GUI_CHOOSE;
        }

        UUID playerUuid = UUID.fromString(uuidString);
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.getConnection() != null) {
            PlayerInfo onlinePlayerInfo = minecraft.getConnection().getPlayerInfo(playerUuid);
            if (onlinePlayerInfo != null) {
                return Component.literal(onlinePlayerInfo.getProfile().getName());
            }
        }

        if (minecraft.getSingleplayerServer() != null) {
            return minecraft.getSingleplayerServer().getProfileCache().get(playerUuid)
                    .map(profile -> (Component) Component.literal(profile.getName()))
                    .orElse(Component.literal(uuidString));
        }

        return Component.literal(uuidString);
    }

    // Items
    static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {

        } else if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {

        } else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {

        } else if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {

        } else if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {

        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            // Parasols
            event.insertBefore(MineraculousItems.LADYBUG_YOYO.toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.WHITE).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.WHITE).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.LIGHT_GRAY).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.LIGHT_GRAY).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.GRAY).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.GRAY).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.BLACK).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.BLACK).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.BROWN).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.BROWN).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.RED).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.RED).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.ORANGE).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.ORANGE).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.YELLOW).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.YELLOW).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.LIME).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.LIME).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.GREEN).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.GREEN).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.CYAN).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.CYAN).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.LIGHT_BLUE).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.LIGHT_BLUE).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.BLUE).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.BLUE).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.PURPLE).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.PURPLE).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.MAGENTA).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.MAGENTA).toStack(), MineraculousKamikotizationsItems.PARASOLS.get(DyeColor.PINK).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(MineraculousItems.BUTTERFLY_CANE.toStack(), MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(MineraculousItems.BUTTERFLY_CANE.toStack(), MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {

        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {

        } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            // Must be in alphabetical order
        } else if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {

        }
    }

    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        List<DeferredItem<ParasolItem>> values = MineraculousKamikotizationsItems.PARASOLS.values().stream().toList();
        Item[] parasols = new Item[values.size() + 1];
        for (int i = 0; i < values.size(); i++) {
            parasols[i] = values.get(i).get();
        }
        parasols[values.size()] = MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL.get();
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0)
                    return HumanoidModel.ArmPose.SPYGLASS;
                return IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
            }
        }, parasols);
    }

    // Entities
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.ICE_CHARGE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.REVERTING_ICE_CHARGE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.REVERTING_LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.REVERTING_WIND_CHARGE.get(), WindChargeRenderer::new);
    }
}
