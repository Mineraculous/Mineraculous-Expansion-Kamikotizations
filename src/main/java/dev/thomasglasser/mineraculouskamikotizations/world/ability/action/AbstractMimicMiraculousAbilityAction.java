package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractMimicMiraculousAbilityAction implements AbilityAction {
    public static final CustomizationSettingAbilityProperty<Holder<Miraculous>> MIRACULOUS = MineraculousKamikotizationsAbilityProperties.MIRACULOUS;
    public static final CustomizationSettingAbilityProperty<UUID> TARGET = MineraculousKamikotizationsAbilityProperties.TARGET;
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(MIRACULOUS);
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(TARGET);

    protected abstract AbilityState createState(CustomizationData customizationData);

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        CustomizationSettings settings = context.source().customizationSettings();
        UUID targetId = settings.get(instance.getOptionalCustomizationSetting(TARGET));
        Holder<Miraculous> miraculousHolder = settings.get(instance.getRequiredCustomizationSetting(MIRACULOUS));
        LivingEntity performer = context.performer();
        MiraculousData sourceData = targetId != null && context.level().getServer().getPlayerList().getPlayer(targetId) instanceof Player player && !player.isRemoved() ? player.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(miraculousHolder) : null;
        CustomizationData customization = sourceData != null ? sourceData.customizationData() : CustomizationData.EMPTY;
        PerformerAbilityData.add(performer, createState(customization));
    }

    protected MiraculousData createProxyData(CustomizationData customizationData, Ability.Context context) {
        return new MiraculousData(customizationData, Optional.empty(), true, Optional.empty(), Optional.empty(), context.source().powerLevel(), true, false, false, -1, ImmutableList.of());
    }

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        return Ability.Result.pass();
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_ACTIVE.get(), MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_PASSIVES.get());
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getRequiredProperties() {
        return REQUIRED_PROPERTIES;
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        return OPTIONAL_PROPERTIES;
    }
}
