package dev.thomasglasser.mineraculouskamikotizations.world.ability.property;

import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import java.util.UUID;
import net.minecraft.core.Holder;

public class MineraculousKamikotizationsAbilityProperties {
    public static final CustomizationSettingAbilityProperty<Holder<Miraculous>> MIRACULOUS = new CustomizationSettingAbilityProperty<>("miraculous", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS);
    public static final CustomizationSettingAbilityProperty<UUID> TARGET = new CustomizationSettingAbilityProperty<>("target", MineraculousKamikotizationsCustomizationSettingTypes.PLAYER);
    public static final CustomizationSettingAbilityProperty<Boolean> TIMER = new CustomizationSettingAbilityProperty<>("timer", MineraculousKamikotizationsCustomizationSettingTypes.TIMER);
}
