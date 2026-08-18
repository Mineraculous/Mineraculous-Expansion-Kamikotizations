package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;

public record MimicMiraculousAbilityState(MiraculousData data) implements AbilityState {
    private static final MapCodec<MimicMiraculousAbilityState> CODEC = MiraculousData.CODEC.fieldOf("miraculousData").xmap(MimicMiraculousAbilityState::new, MimicMiraculousAbilityState::data);
    public static final Type<MimicMiraculousAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS.get();
    }
}
