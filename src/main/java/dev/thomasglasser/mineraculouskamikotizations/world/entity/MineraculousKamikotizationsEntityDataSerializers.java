package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import dev.thomasglasser.tommylib.api.util.TommyLibExtraStreamCodecs;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

public class MineraculousKamikotizationsEntityDataSerializers {
    private static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<BubblePrison.BubbleBehavior>> BUBBLE_PRISON_BEHAVIOR = ENTITY_DATA_SERIALIZERS.register("bubble_prison_behavior", () -> EntityDataSerializer.forValueType(TommyLibExtraStreamCodecs.forEnum(BubblePrison.BubbleBehavior.class)));
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<UUID>>> BUBBLE_PRISON_CAPTIVES = ENTITY_DATA_SERIALIZERS.register("bubble_prison_captives", () -> EntityDataSerializer.forValueType(UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list())));

    @ApiStatus.Internal
    public static void init() {}
}
