package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;

public record MimicMiraculousActiveAbilityState(MiraculousData data) implements AbilityState {
    private static final MapCodec<MimicMiraculousActiveAbilityState> CODEC = MiraculousData.CODEC.fieldOf("miraculousData").xmap(MimicMiraculousActiveAbilityState::new, MimicMiraculousActiveAbilityState::data);
    public static final Type<MimicMiraculousActiveAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousActiveAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_ACTIVE.get();
    }
}
