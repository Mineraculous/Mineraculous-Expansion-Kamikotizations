package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record VisualMiraculousMimicAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<VisualMiraculousMimicAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customizationData").xmap(VisualMiraculousMimicAbilityState::new, VisualMiraculousMimicAbilityState::customizationData);
    public static final Type<VisualMiraculousMimicAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<VisualMiraculousMimicAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.VISUAL_MIRACULOUS_MIMIC.get();
    }
}
