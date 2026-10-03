package dev.thomasglasser.mineraculouskamikotizations.world.targetvisual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record VisualTarget(Holder<Miraculous> miraculous, UUID uuid) {
    public static final Codec<VisualTarget> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Miraculous.CODEC.fieldOf("miraculous").forGetter(VisualTarget::miraculous),
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(VisualTarget::uuid)).apply(instance, VisualTarget::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, VisualTarget> STREAM_CODEC = StreamCodec.composite(
            Miraculous.STREAM_CODEC,
            VisualTarget::miraculous,
            UUIDUtil.STREAM_CODEC,
            VisualTarget::uuid,
            VisualTarget::new);
}
