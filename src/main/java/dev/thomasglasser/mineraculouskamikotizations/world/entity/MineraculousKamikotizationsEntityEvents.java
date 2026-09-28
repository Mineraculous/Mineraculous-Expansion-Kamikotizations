package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.kamikotization.KamikotizationData;
import dev.thomasglasser.mineraculous.api.world.kamikotization.KamikotizationsData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameColorAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class MineraculousKamikotizationsEntityEvents {
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        Player player = event.getEntity();
        KamikotizationsData kamikotizationsData = player.getExistingDataOrNull(MineraculousAttachmentTypes.KAMIKOTIZATIONS);
        if (kamikotizationsData == null || kamikotizationsData.getCurrent().isEmpty()) {
            return;
        }
        MimicMiraculousNameColorAbilityState nameColorAbilityState = PerformerAbilityData.get(player, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME_COLOR);
        MimicMiraculousNameAbilityState nameAbilityState = PerformerAbilityData.get(player, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME);
        KamikotizationData data = kamikotizationsData.getCurrent().get();
        CustomizationData customizationData = data.customizationData();
        String name = customizationData.nameSetting().map(customizationData.settings()::get).orElse(null);
        if (name == null || name.isEmpty()) {
            name = event.getDisplayname().getString();
        }
        event.setDisplayname(Entity.removeAction((nameAbilityState != null ? nameAbilityState.name() : Component.literal(name)).copy().setStyle((nameAbilityState != null ? nameAbilityState.name().getStyle() : Style.EMPTY).withColor(nameColorAbilityState != null ? nameColorAbilityState.nameColor() : data.nameColor()))));
    }
}
