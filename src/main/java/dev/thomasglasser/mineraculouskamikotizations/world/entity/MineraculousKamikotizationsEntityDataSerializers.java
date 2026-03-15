package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.level.storage.BubblePrisonData;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;

public class MineraculousKamikotizationsEntityDataSerializers {
    private static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<BubblePrisonData>> BUBBLE_PRISON_DATA = ENTITY_DATA_SERIALIZERS.register("bubble_prison_data", () -> EntityDataSerializer.forValueType(BubblePrisonData.STREAM_CODEC));

    @ApiStatus.Internal
    public static void init() {}
}
