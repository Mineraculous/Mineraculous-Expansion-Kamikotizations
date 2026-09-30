package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record MimicMiraculousLooksAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<MimicMiraculousLooksAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customization_data").xmap(MimicMiraculousLooksAbilityState::new, MimicMiraculousLooksAbilityState::customizationData);
    public static final Type<MimicMiraculousLooksAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousLooksAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_LOOKS.get();
    }
}
