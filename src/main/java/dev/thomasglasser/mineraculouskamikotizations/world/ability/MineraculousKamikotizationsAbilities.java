package dev.thomasglasser.mineraculouskamikotizations.world.ability;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityBranch;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MimicActiveMiraculousAbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MimicPassiveMiraculousAbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MimicVisualMiraculousAbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MineraculousKamikotizationsAbilityActions;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class MineraculousKamikotizationsAbilities {
    public static final ResourceKey<Ability> MIMIC_ACTIVE_MIRACULOUS = create("mimic_active_miraculous");
    public static final ResourceKey<Ability> MIMIC_PASSIVE_MIRACULOUS = create("mimic_passive_miraculous");
    public static final ResourceKey<Ability> MIMIC_VISUAL_MIRACULOUS = create("mimic_visual_miraculous");

    public static void bootstrap(BootstrapContext<Ability> context) {
        HolderGetter<AbilityAction> abilityActions = context.lookup(MineraculousRegistries.ABILITY_ACTION);

        context.register(MIMIC_ACTIVE_MIRACULOUS, Ability.builder()
                .branch(AbilityBranch.builder()
                        .action(abilityActions.getOrThrow(MineraculousKamikotizationsAbilityActions.MIMIC_ACTIVE_MIRACULOUS.getKey()), AbilityProperties.builder()
                                .add(MimicActiveMiraculousAbilityAction.MIRACULOUS, "miraculous")
                                .add(MimicActiveMiraculousAbilityAction.TRANSFORMATION_ENTRY, "target")
                                .add(MimicActiveMiraculousAbilityAction.ENABLE_TIMER, "timer")))
                .customizationSetting("miraculous", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS, Optional.empty(), false)
                .customizationSetting("target", MineraculousKamikotizationsCustomizationSettingTypes.TRANSFORMATION_ENTRY, Optional.empty(), true)
                .customizationSetting("timer", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN, Optional.of(false), false)
                .build());

        context.register(MIMIC_PASSIVE_MIRACULOUS, Ability.builder()
                .branch(AbilityBranch.builder()
                        .action(abilityActions.getOrThrow(MineraculousKamikotizationsAbilityActions.MIMIC_PASSIVE_MIRACULOUS.getKey()), AbilityProperties.builder()
                                .add(MimicPassiveMiraculousAbilityAction.MIRACULOUS, "miraculous")
                                .add(MimicPassiveMiraculousAbilityAction.TRANSFORMATION_ENTRY, "target")))
                .customizationSetting("miraculous", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS, Optional.empty(), false)
                .customizationSetting("target", MineraculousKamikotizationsCustomizationSettingTypes.TRANSFORMATION_ENTRY, Optional.empty(), true)
                .build());

        context.register(MIMIC_VISUAL_MIRACULOUS, Ability.builder()
                .branch(AbilityBranch.builder()
                        .action(abilityActions.getOrThrow(MineraculousKamikotizationsAbilityActions.MIMIC_VISUAL_MIRACULOUS.getKey()), AbilityProperties.builder()
                                .add(MimicVisualMiraculousAbilityAction.MIRACULOUS, "miraculous")
                                .add(MimicVisualMiraculousAbilityAction.TRANSFORMATION_ENTRY, "target")
                                .add(MimicVisualMiraculousAbilityAction.LOOKS, "looks")
                                .add(MimicVisualMiraculousAbilityAction.COPY_NAME_COLOR, "copy_name_color")))
                .customizationSetting("miraculous", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS, Optional.empty(), false)
                .customizationSetting("target", MineraculousKamikotizationsCustomizationSettingTypes.TRANSFORMATION_ENTRY, Optional.empty(), true)
                .customizationSetting("looks", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN, Optional.of(true), true)
                .customizationSetting("copy_name_color", MineraculousKamikotizationsCustomizationSettingTypes.BOOLEAN, Optional.of(true), false)
                .build());
    }

    private static ResourceKey<Ability> create(String name) {
        return ResourceKey.create(MineraculousRegistries.ABILITY, MineraculousKamikotizations.modLoc(name));
    }
}
