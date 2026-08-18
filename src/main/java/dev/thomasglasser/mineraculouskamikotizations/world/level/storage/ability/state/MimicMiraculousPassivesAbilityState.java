package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import dev.thomasglasser.mineraculous.api.world.miraculous.MiraculousData;

public record MimicMiraculousPassivesAbilityState(MiraculousData data) implements AbilityState {
    private static final MapCodec<MimicMiraculousPassivesAbilityState> CODEC = MiraculousData.CODEC.fieldOf("miraculousData").xmap(MimicMiraculousPassivesAbilityState::new, MimicMiraculousPassivesAbilityState::data);
    public static final Type<MimicMiraculousPassivesAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousPassivesAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_PASSIVES.get();
    }
}
