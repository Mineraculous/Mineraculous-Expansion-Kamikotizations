package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;

public record MimicAbilityState(MiraculousData data) implements AbilityState {
    public static final Type<MimicAbilityState> TYPE = Type.temporary();

    @Override
    public Type<MimicAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC.get();
    }
}
