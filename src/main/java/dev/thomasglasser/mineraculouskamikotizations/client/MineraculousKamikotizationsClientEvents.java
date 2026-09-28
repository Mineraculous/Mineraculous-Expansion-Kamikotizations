package dev.thomasglasser.mineraculouskamikotizations.client;

import dev.thomasglasser.mineraculous.api.client.gui.components.CustomizationSettingWidgetRegistry;
import dev.thomasglasser.mineraculous.api.client.look.util.KamikotizationToolClientExtensions;
import dev.thomasglasser.mineraculous.api.client.renderer.item.KamikotizationToolLookGeoItemRenderer;
import dev.thomasglasser.mineraculous.api.world.item.MineraculousItems;
import dev.thomasglasser.mineraculous.impl.world.item.component.Active;
import dev.thomasglasser.mineraculouskamikotizations.client.renderer.item.MineraculousKamikotizationsItemProperties;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.MineraculousKamikotizationsEntityTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.item.MineraculousKamikotizationsItems;
import dev.thomasglasser.mineraculouskamikotizations.world.item.WeatherControlParasolItem;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.WindChargeRenderer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.Nullable;

public class MineraculousKamikotizationsClientEvents {
    // Init
    static void onFMLClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MineraculousKamikotizationsItemProperties.init();

            // Customization Setting Widgets
            CustomizationSettingWidgetRegistry.register(MineraculousKamikotizationsCustomizationSettingTypes.VISUAL_TARGET.get(), (property, screen, font, width, height, currentValue, onChanged) -> MineraculousKamikotizationsClientUtils.createVisualTargetButton(screen, property.optional(), null, currentValue, width, height, onChanged));

            CustomizationSettingWidgetRegistry.register(MineraculousKamikotizationsCustomizationSettingTypes.POWER_TARGET.get(), (property, screen, font, width, height, currentValue, onChanged) -> MineraculousKamikotizationsClientUtils.createPowerTargetButton(screen, false, currentValue, 150, 40, onChanged));

            CustomizationSettingWidgetRegistry.register(MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN.get(), (setting, screen, font, width, height, currentValue, onChanged) -> CycleButton.onOffBuilder(currentValue != null && currentValue).displayOnlyValue().create(0, 0, width, height, Component.empty(), (button, value) -> onChanged.accept(value)));
        });
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
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0)
                    return HumanoidModel.ArmPose.SPYGLASS;
                return IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
            }
        }, MineraculousKamikotizationsItems.PARASOLS.values().toArray(Holder[]::new));
        event.registerItem(new KamikotizationToolClientExtensions<KamikotizationToolLookGeoItemRenderer<WeatherControlParasolItem>>() {
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (Active.isActive(itemStack) && entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0)
                    return HumanoidModel.ArmPose.SPYGLASS;
                return super.getArmPose(entityLiving, hand, itemStack);
            }
        }, MineraculousKamikotizationsItems.WEATHER_CONTROL_PARASOL);
    }

    // Entities
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.ICE_CHARGE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.REVERTING_ICE_CHARGE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.REVERTING_LIGHTNING_BOLT.get(), LightningBoltRenderer::new);
        event.registerEntityRenderer(MineraculousKamikotizationsEntityTypes.REVERTING_WIND_CHARGE.get(), WindChargeRenderer::new);
    }
}
