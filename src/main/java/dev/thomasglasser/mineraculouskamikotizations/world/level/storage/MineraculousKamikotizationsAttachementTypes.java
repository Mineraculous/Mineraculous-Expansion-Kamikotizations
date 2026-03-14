package dev.thomasglasser.mineraculouskamikotizations.world.level.storage;

import com.mojang.serialization.Codec;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

public class MineraculousKamikotizationsAttachementTypes {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MineraculousKamikotizations.MOD_ID);

    // held by entities imprisoned inside a BubblePrison
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<BubbleImprisonedData>>> BUBBLE_DATA = ATTACHMENT_TYPES.register("bubble_uuid", () -> AttachmentType.builder(Optional::<BubbleImprisonedData>empty).serialize(optionalCodec(BubbleImprisonedData.CODEC)).sync(optionalStreamCodec(BubbleImprisonedData.STREAM_CODEC)).build());

    private static <T> Codec<Optional<T>> optionalCodec(Codec<T> codec) {
        return codec.optionalFieldOf("data").codec();
    }

    private static <B extends ByteBuf, T> StreamCodec<B, Optional<T>> optionalStreamCodec(StreamCodec<B, T> codec) {
        return ByteBufCodecs.optional(codec);
    }

    @ApiStatus.Internal
    public static void init() {}
}
