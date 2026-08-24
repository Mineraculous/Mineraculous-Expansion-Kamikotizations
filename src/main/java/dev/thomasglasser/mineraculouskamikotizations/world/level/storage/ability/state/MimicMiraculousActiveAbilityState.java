package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record MimicMiraculousActiveAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<MimicMiraculousActiveAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customizationData").xmap(MimicMiraculousActiveAbilityState::new, MimicMiraculousActiveAbilityState::customizationData);
    public static final Type<MimicMiraculousActiveAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousActiveAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_ACTIVE.get();
    }
}
