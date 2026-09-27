package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.ability.state;

import com.mojang.serialization.MapCodec;
import dev.thomasglasser.mineraculous.api.world.level.storage.ability.state.AbilityState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record MimicMiraculousNameAbilityState(Component name) implements AbilityState {
    private static final MapCodec<MimicMiraculousNameAbilityState> CODEC = ComponentSerialization.CODEC.fieldOf("name").xmap(MimicMiraculousNameAbilityState::new, MimicMiraculousNameAbilityState::name);
    public static final Type<MimicMiraculousNameAbilityState> TYPE = Type.persistent(CODEC);

    @Override
    public Type<MimicMiraculousNameAbilityState> type() {
        return MineraculousKamikotizationsAbilityStates.MIMIC_MIRACULOUS_NAME.get();
    }
}
