package dev.thomasglasser.mineraculouskamikotizations.world.ability.property;

import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import net.minecraft.core.Holder;

// TODO: move to main mod
public class MineraculousKamikotizationsAbilityProperties {
    public static final CustomizationSettingAbilityProperty<Holder<Miraculous>> MIRACULOUS = new CustomizationSettingAbilityProperty<>("miraculous", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS);
    public static final CustomizationSettingAbilityProperty<TransformationHistoryData.TransformationEntry> TARGET = new CustomizationSettingAbilityProperty<>("target", MineraculousKamikotizationsCustomizationSettingTypes.TRANSFORMATION_ENTRY);
    public static final CustomizationSettingAbilityProperty<Boolean> ENABLE_TIMER = new CustomizationSettingAbilityProperty<>("enable_timer", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_LOOKS = new CustomizationSettingAbilityProperty<>("copy_looks", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_NAME_COLOR = new CustomizationSettingAbilityProperty<>("copy_name_color", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
}
