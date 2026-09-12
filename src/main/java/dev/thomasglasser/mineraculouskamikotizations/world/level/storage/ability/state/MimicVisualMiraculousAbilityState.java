package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record MimicVisualMiraculousAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<MimicVisualMiraculousAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customization_data").xmap(MimicVisualMiraculousAbilityState::new, MimicVisualMiraculousAbilityState::customizationData);
    public static final Type<MimicVisualMiraculousAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicVisualMiraculousAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_VISUAL_MIRACULOUS.get();
    }
}
