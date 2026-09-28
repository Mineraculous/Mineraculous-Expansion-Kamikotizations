package dev.thomasglasser.mineraculouskamikotizations.world.ability.property;

import com.mojang.datafixers.util.Either;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.targetvisual.VisualTarget;

public class MineraculousKamikotizationsAbilityProperties {
    public static final CustomizationSettingAbilityProperty<Either<VisualTarget, TransformationHistoryData.TransformationEntry>> VISUAL_TARGET = new CustomizationSettingAbilityProperty<>("visual_target", MineraculousKamikotizationsCustomizationSettingTypes.VISUAL_TARGET);
    public static final CustomizationSettingAbilityProperty<TransformationHistoryData.TransformationEntry> POWER_TARGET = new CustomizationSettingAbilityProperty<>("power_target", MineraculousKamikotizationsCustomizationSettingTypes.POWER_TARGET);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_TIMER = new CustomizationSettingAbilityProperty<>("copy_timer", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_LOOKS = new CustomizationSettingAbilityProperty<>("copy_looks", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_SKIN = new CustomizationSettingAbilityProperty<>("copy_skin", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_NAME_COLOR = new CustomizationSettingAbilityProperty<>("copy_name_color", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_NAME = new CustomizationSettingAbilityProperty<>("copy_name", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_TOOL = new CustomizationSettingAbilityProperty<>("copy_tool", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN);
}
