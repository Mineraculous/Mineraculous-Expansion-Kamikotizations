package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameColorAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class MineraculousKamikotizationEntityUtils {
    /**
     * Formats the provided {@link Entity}'s display name,
     * taking the naming ability states into account.
     *
     * @param entity   The entity to check for formatting
     * @param original The original name of the entity
     * @return The formatted name of the entity, or the original if no formatting was needed
     */
    public static Component formatDisplayName(Entity entity, @Nullable Component original) {
        if (original != null && entity instanceof Player player) {
            MimicMiraculousNameColorAbilityState nameColorAbilityState = PerformerAbilityData.get(player, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME_COLOR);
            MimicMiraculousNameAbilityState nameAbilityState = PerformerAbilityData.get(player, MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME);
            Style style = original.getStyle();
            TextColor textColor = style.getColor();
            return Entity.removeAction((nameAbilityState != null ? nameAbilityState.name() : original).copy().setStyle(nameAbilityState != null ? nameAbilityState.name().getStyle().withColor(nameColorAbilityState != null ? TextColor.fromRgb(nameColorAbilityState.nameColor()) : textColor != null ? textColor : TextColor.fromRgb(0xFFFFFF)) : style));
        }
        return original;
    }
}
