package dev.thomasglasser.mineraculouskamikotizations.mixin.mineraculous.api.world.entity;

import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.entity.MineraculousEntityUtils;
import dev.thomasglasser.mineraculous.api.world.kamikotization.KamikotizationData;
import dev.thomasglasser.mineraculous.api.world.kamikotization.KamikotizationsData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameColorAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MineraculousEntityUtils.class)
public class MineraculousEntityUtilsMixin {
    @Inject(method = "formatDisplayName", at = @At("HEAD"), cancellable = true)
    private static void overridePlayerName(Entity entity, Component original, CallbackInfoReturnable<Component> cir) {
        KamikotizationsData kamikotizationsData = entity.getExistingDataOrNull(MineraculousAttachmentTypes.KAMIKOTIZATIONS);
        if (kamikotizationsData == null || kamikotizationsData.getCurrent().isEmpty()) {
            return;
        }
        MimicMiraculousNameColorAbilityState nameColorAbilityState = PerformerAbilityData.get(entity, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME_COLOR);
        MimicMiraculousNameAbilityState nameAbilityState = PerformerAbilityData.get(entity, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME);
        KamikotizationData data = kamikotizationsData.getCurrent().get();
        CustomizationData customizationData = data.customizationData();
        String name = customizationData.nameSetting().map(customizationData.settings()::get).orElse(null);
        if (name == null || name.isEmpty()) {
            name = original.getString();
        }
        cir.setReturnValue(Entity.removeAction((nameAbilityState != null ? nameAbilityState.name() : Component.literal(name)).copy().setStyle(Style.EMPTY.withColor(nameColorAbilityState != null ? nameColorAbilityState.nameColor() : data.nameColor()))));
    }
}
