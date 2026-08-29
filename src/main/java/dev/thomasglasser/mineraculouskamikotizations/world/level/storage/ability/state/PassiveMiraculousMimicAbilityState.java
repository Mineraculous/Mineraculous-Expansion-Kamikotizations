package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record PassiveMiraculousMimicAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<PassiveMiraculousMimicAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customizationData").xmap(PassiveMiraculousMimicAbilityState::new, PassiveMiraculousMimicAbilityState::customizationData);
    public static final Type<PassiveMiraculousMimicAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<PassiveMiraculousMimicAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.PASSIVE_MIRACULOUS_MIMIC.get();
    }
}
