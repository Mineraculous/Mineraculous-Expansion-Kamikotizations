package dev.thomasglasser.mineraculouskamikotizations.event;

import com.google.common.collect.ImmutableMap;
import dev.thomasglasser.mineraculous.api.core.component.MineraculousDataComponents;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.core.look.context.LookContext;
import dev.thomasglasser.mineraculous.api.core.look.context.LookContexts;
import dev.thomasglasser.mineraculous.api.event.KamikotizationEvent;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.kamikotization.KamikotizationData;
import dev.thomasglasser.mineraculous.api.world.kamikotization.KamikotizationsData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.impl.server.look.ServerLookManager;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousLooksAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Optional;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MineraculousKamikotizationsKamikotizationEvents {
    public static void onFinishKamikotizationTransform(KamikotizationEvent.Transform.Finish event) {
        LivingEntity livingEntity = event.getEntity();
        MimicMiraculousLooksAbilityState looksAbilityState = PerformerAbilityData.get(livingEntity, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_LOOKS);
        if (looksAbilityState != null) {
            CustomizationData customizationData = looksAbilityState.customizationData();
            KamikotizationData kamikotizationData = updateKamikotizationLooks(livingEntity.getData(MineraculousAttachmentTypes.KAMIKOTIZATIONS), ImmutableMap.copyOf(new Object2ObjectOpenHashMap<>(customizationData.looks()))).get();
            CustomizationData newCustomizationData = kamikotizationData.customizationData();
            for (EquipmentSlot slot : new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }) {
                ItemStack newStack = livingEntity.getItemBySlot(slot).copy();
                newStack.set(MineraculousDataComponents.CUSTOMIZATION_DATA, newCustomizationData);
                livingEntity.setItemSlot(slot, newStack);
            }
            kamikotizationData.save(livingEntity);
            if (livingEntity instanceof ServerPlayer player) {
                player.getData(MineraculousAttachmentTypes.KAMIKOTIZATIONS).getCurrent().ifPresent(data -> ServerLookManager.requestMissingLooks(data.customizationData(), player));
                ServerLookManager.sendServerLooks(player);
            }
        }
    }

    private static Optional<KamikotizationData> updateKamikotizationLooks(KamikotizationsData kamikotizationsData, ImmutableMap<ResourceKey<LookContext>, ResourceLocation> looks) {
        Optional<KamikotizationData> kamikotizationData = kamikotizationsData.getCurrent();
        if (kamikotizationData.isPresent()) {
            KamikotizationData currentKamikotizationData = kamikotizationData.get();
            ResourceKey<LookContext> kamikotizationSuitKey = LookContexts.KAMIKOTIZATION_SUIT.getKey();
            ResourceKey<LookContext> miraculousSuitKey = LookContexts.MIRACULOUS_SUIT.getKey();
            ResourceLocation miraculousSuitLocation = looks.get(miraculousSuitKey);
            if (miraculousSuitKey != null && kamikotizationSuitKey != null && miraculousSuitLocation != null) {
                KamikotizationData newKamikotizationData = new KamikotizationData(
                        currentKamikotizationData.kamikotization(),
                        currentKamikotizationData.giverData(),
                        new CustomizationData(
                                currentKamikotizationData.customizationData().nameSetting(),
                                currentKamikotizationData.customizationData().settings(),
                                ImmutableMap.<ResourceKey<LookContext>, ResourceLocation>builder()
                                        .putAll(looks)
                                        .put(kamikotizationSuitKey, miraculousSuitLocation).build()),
                        currentKamikotizationData.nameColor(),
                        currentKamikotizationData.transformationState(),
                        currentKamikotizationData.revertibleId(),
                        currentKamikotizationData.kamikotizedSlot(),
                        currentKamikotizationData.originalStackCount(),
                        currentKamikotizationData.remainingStackCount(),
                        currentKamikotizationData.brokenKamikotizedStack(),
                        currentKamikotizationData.powerLevel(),
                        currentKamikotizationData.powerActive(),
                        currentKamikotizationData.buffsActive());
                return Optional.of(newKamikotizationData);
            }
        }
        return kamikotizationData;
    }
}
