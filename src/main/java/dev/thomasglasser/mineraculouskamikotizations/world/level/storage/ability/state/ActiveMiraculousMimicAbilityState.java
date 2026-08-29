package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record ActiveMiraculousMimicAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<ActiveMiraculousMimicAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customizationData").xmap(ActiveMiraculousMimicAbilityState::new, ActiveMiraculousMimicAbilityState::customizationData);
    public static final Type<ActiveMiraculousMimicAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<ActiveMiraculousMimicAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.ACTIVE_MIRACULOUS_MIMIC.get();
    }
}
