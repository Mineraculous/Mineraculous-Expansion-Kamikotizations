package dev.thomasglasser.mineraculouskamikotizations.world.ability.property;

import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculous.api.world.ability.property.AbilityProperty;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

public class PlayerAbilityProperty extends AbilityProperty<UUID> {
    private static final StreamCodec<RegistryFriendlyByteBuf, UUID> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString).cast();

    public PlayerAbilityProperty(String name, @Nullable UUID defaultValue) {
        super(name, defaultValue);
    }

    public PlayerAbilityProperty(String name) {
        this(name, null);
    }

    @Override
    public Codec<UUID> codec() {
        return UUIDUtil.STRING_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, UUID> streamCodec() {
        return STREAM_CODEC;
    }
}
