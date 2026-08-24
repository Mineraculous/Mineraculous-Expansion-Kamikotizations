package dev.thomasglasser.mineraculouskamikotizations.world.ability;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityBranch;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MimicMiraculousActiveAbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MimicMiraculousPassivesAbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MineraculousKamikotizationsAbilityActions;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class MineraculousKamikotizationsAbilities {
    public static final ResourceKey<Ability> MIMIC_MIRACULOUS_ACTIVE = create("mimic_miraculous_active");
    public static final ResourceKey<Ability> MIMIC_MIRACULOUS_PASSIVES = create("mimic_miraculous_passives");

    private static ResourceKey<Ability> create(String name) {
        return ResourceKey.create(MineraculousRegistries.ABILITY, MineraculousKamikotizations.modLoc(name));
    }

    public static void bootstrap(BootstrapContext<Ability> context) {
        HolderGetter<AbilityAction> abilityActions = context.lookup(MineraculousRegistries.ABILITY_ACTION);

        context.register(MIMIC_MIRACULOUS_ACTIVE, Ability.builder()
                .branch(AbilityBranch.builder().action(abilityActions.getOrThrow(MineraculousKamikotizationsAbilityActions.MIMIC_MIRACULOUS_ACTIVE.getKey()), AbilityProperties.builder()
                        .add(MimicMiraculousActiveAbilityAction.MIRACULOUS, "miraculous_active")
                        .add(MimicMiraculousActiveAbilityAction.TARGET, "target")
                        .add(MimicMiraculousActiveAbilityAction.TIMER, "timer")))
                .customizationSetting("miraculous_active", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS, Optional.empty(), false)
                .customizationSetting("target", MineraculousKamikotizationsCustomizationSettingTypes.PLAYER, Optional.empty(), true)
                .customizationSetting("timer", MineraculousKamikotizationsCustomizationSettingTypes.TIMER, Optional.of(false), true)

                .build());

        context.register(MIMIC_MIRACULOUS_PASSIVES, Ability.builder()
                .branch(AbilityBranch.builder().action(abilityActions.getOrThrow(MineraculousKamikotizationsAbilityActions.MIMIC_MIRACULOUS_PASSIVES.getKey()), AbilityProperties.builder()
                        .add(MimicMiraculousPassivesAbilityAction.MIRACULOUS, "miraculous_passives")
                        .add(MimicMiraculousPassivesAbilityAction.TARGET, "target")))
                .customizationSetting("miraculous_passives", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS, Optional.empty(), false)
                .customizationSetting("target", MineraculousKamikotizationsCustomizationSettingTypes.PLAYER, Optional.empty(), true)
                .build());
    }
}
