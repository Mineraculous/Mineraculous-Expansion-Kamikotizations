package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record MimicMiraculousNameColorAbilityState(int nameColor) implements AbilityState {
    private static final MapCodec<MimicMiraculousNameColorAbilityState> CODEC = Codec.INT.fieldOf("name_color").xmap(MimicMiraculousNameColorAbilityState::new, MimicMiraculousNameColorAbilityState::nameColor);
    public static final Type<MimicMiraculousNameColorAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousNameColorAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME_COLOR.get();
    }
}
