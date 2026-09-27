package dev.thomasglasser.mineraculouskamikotizations.world.ability.action;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.core.customization.setting.CustomizationSettings;
import dev.thomasglasser.mineraculous.api.world.ability.Ability;
import dev.thomasglasser.mineraculous.api.world.ability.action.AbilityAction;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityPropertyInstance;
import dev.thomasglasser.mineraculous.api.world.ability.property.CustomizationSettingAbilityProperty;
import dev.thomasglasser.mineraculous.api.world.attachment.MineraculousAttachmentTypes;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.PerformerAbilityData;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.world.ability.property.MineraculousKamikotizationsAbilityProperties;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.TransformationHistoryData;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MimicMiraculousNameColorAbilityState;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state.MineraculousKamikotizationsAbilityStates;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MimicVisualMiraculousAbilityAction extends AbstractMimicMiraculousAbilityAction {
    public static final CustomizationSettingAbilityProperty<Boolean> LOOKS = MineraculousKamikotizationsAbilityProperties.COPY_LOOKS;
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_NAME_COLOR = MineraculousKamikotizationsAbilityProperties.COPY_NAME_COLOR;
    public static final CustomizationSettingAbilityProperty<Boolean> COPY_NAME = MineraculousKamikotizationsAbilityProperties.COPY_NAME;
    public static final CustomizationSettingAbilityProperty<Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry>> VISUAL_TARGET = MineraculousKamikotizationsAbilityProperties.VISUAL_TARGET;
    private static final ImmutableSet<AbilityProperty<?>> OPTIONAL_PROPERTIES = ImmutableSet.of(LOOKS);
    private static final ImmutableSet<AbilityProperty<?>> REQUIRED_PROPERTIES = ImmutableSet.of(VISUAL_TARGET, COPY_NAME_COLOR, COPY_NAME);

    @Override
    public Ability.Result perform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        return Ability.Result.fail();
    }

    @Override
    public void transform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        super.transform(instance, context);
        LivingEntity performer = context.performer();
        CustomizationSettings customizationSettings = context.source().customizationSettings();
        Either<Pair<Holder<Miraculous>, UUID>, TransformationHistoryData.TransformationEntry> either = customizationSettings.get(instance.getRequiredCustomizationSetting(VISUAL_TARGET));
        if (either != null) {
            either.ifRight(entry -> {
                if (Boolean.TRUE.equals(customizationSettings.get(instance.getRequiredCustomizationSetting(COPY_NAME_COLOR)))) {
                    PerformerAbilityData.add(performer, new MimicMiraculousNameColorAbilityState(entry.miraculous().value().color().getValue()));
                }
                if (Boolean.TRUE.equals(customizationSettings.get(instance.getRequiredCustomizationSetting(COPY_NAME)))) {
                    PerformerAbilityData.add(performer, new MimicMiraculousNameAbilityState(entry.getName()));
                }
            });
            either.ifLeft(pair -> {
                UUID uuid = pair.getSecond();
                Player player = context.level().getServer().getPlayerList().getPlayer(uuid);
                Holder<Miraculous> miraculous = pair.getFirst();
                if (player != null) {
                    CustomizationData customizationData = player.getData(MineraculousAttachmentTypes.MIRACULOUSES).get(miraculous).customizationData();
                    String name = customizationData.nameSetting().map(customizationData.settings()::get).orElse(null);
                    if (Boolean.TRUE.equals(customizationSettings.get(instance.getRequiredCustomizationSetting(COPY_NAME_COLOR)))) {
                        PerformerAbilityData.add(performer, new MimicMiraculousNameColorAbilityState(miraculous.value().color().getValue()));
                    }
                    if (Boolean.TRUE.equals(customizationSettings.get(instance.getRequiredCustomizationSetting(COPY_NAME))) && name != null) {
                        PerformerAbilityData.add(performer, new MimicMiraculousNameAbilityState(Component.literal(name)));
                    }
                }
            });
            if (performer instanceof Player player) {
                player.refreshDisplayName();
            }
        }
        // TODO: Looks logic
    }

    @Override
    public void detransform(AbilityPropertyInstance<AbilityAction> instance, Ability.Context context) {
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_VISUAL_MIRACULOUS.get());
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME_COLOR.get());
        PerformerAbilityData.remove(context.performer(), MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME.get());
        super.detransform(instance, context);
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getOptionalProperties() {
        return OPTIONAL_PROPERTIES;
    }

    @Override
    public ImmutableSet<AbilityProperty<?>> getRequiredProperties() {
        return REQUIRED_PROPERTIES;
    }
}
