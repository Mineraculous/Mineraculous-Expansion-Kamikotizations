package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record MimicMiraculousPassivesAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<MimicMiraculousPassivesAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customizationData").xmap(MimicMiraculousPassivesAbilityState::new, MimicMiraculousPassivesAbilityState::customizationData);
    public static final Type<MimicMiraculousPassivesAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousPassivesAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_PASSIVES.get();
    }
}
