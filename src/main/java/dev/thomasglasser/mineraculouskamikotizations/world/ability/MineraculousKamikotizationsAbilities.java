package dev.thomasglasser.mineraculouskamikotizations.world.ability;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.AbilityBranch;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.core.customization.setting.MineraculousKamikotizationsCustomizationSettingTypes;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MimicActiveAbilityAction;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.action.MineraculousKamikotizationsAbilityActions;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class MineraculousKamikotizationsAbilities {
    public static final ResourceKey<Ability> REPLICATE_ACTIVE = create("replicate_active");

    private static ResourceKey<Ability> create(String name) {
        return ResourceKey.create(MineraculousRegistries.ABILITY, MineraculousKamikotizations.modLoc(name));
    }

    public static void bootstrap(BootstrapContext<Ability> context) {
        Holder.Reference<AbilityAction> replicateActive = context.lookup(MineraculousRegistries.ABILITY_ACTION).getOrThrow(MineraculousKamikotizationsAbilityActions.REPLICATE_ACTIVE.getKey());

        context.register(REPLICATE_ACTIVE, Ability.builder()
                .branch(AbilityBranch.builder().action(replicateActive, AbilityProperties.builder()
                        .add(MimicActiveAbilityAction.MIRACULOUS, "miraculous")))
                .customizationSetting("miraculous", MineraculousKamikotizationsCustomizationSettingTypes.MIRACULOUS, Optional.empty(), false)
                .customizationSetting("player", MineraculousKamikotizationsCustomizationSettingTypes.PLAYER, Optional.empty(), true)
                .build());
    }
}
