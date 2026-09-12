package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;

public record MimicActiveMiraculousAbilityState(CustomizationData customizationData) implements AbilityState {
    private static final MapCodec<MimicActiveMiraculousAbilityState> CODEC = CustomizationData.CODEC.fieldOf("customizationData").xmap(MimicActiveMiraculousAbilityState::new, MimicActiveMiraculousAbilityState::customizationData);
    public static final Type<MimicActiveMiraculousAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicActiveMiraculousAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_ACTIVE_MIRACULOUS.get();
    }
}
