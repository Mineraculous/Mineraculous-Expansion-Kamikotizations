package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record MimicPassiveMiraculousAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<MimicPassiveMiraculousAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customization_data").xmap(MimicPassiveMiraculousAbilityState::new, MimicPassiveMiraculousAbilityState::customizationData);
    public static final Type<MimicPassiveMiraculousAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicPassiveMiraculousAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_PASSIVE_MIRACULOUS.get();
    }
}
