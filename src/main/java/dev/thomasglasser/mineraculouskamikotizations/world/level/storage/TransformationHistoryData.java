package dev.thomasglasser.mineraculouskamikotizations.world.level.storage;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class TransformationHistoryData extends SavedData {
    private static final String FILE_ID = "transformation_history";
    private static final Codec<Map<UUID, TransformationEntry>> MAP_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, TransformationEntry.CODEC);

    private final SetMultimap<UUID, TransformationEntry> historyIndex = HashMultimap.create();
    private final Map<UUID, TransformationEntry> entryById = new Object2ObjectOpenHashMap<>();

    public static TransformationHistoryData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(new Factory<>(TransformationHistoryData::new, TransformationHistoryData::load, DataFixTypes.LEVEL), FILE_ID);
    }

    /// Records a transformation event if an identical one doesn't already exist for this player.
    public void recordTransformation(UUID ownerUuid, Holder<Miraculous> miraculous, @Nullable CustomizationData customizationData) {
        Optional<CustomizationData> optionalCustomizationData = Optional.ofNullable(customizationData);
        boolean exists = false;
        for (TransformationEntry entry : historyIndex.get(ownerUuid)) {
            if (entry.miraculous().equals(miraculous) && entry.customizationData().equals(optionalCustomizationData)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            TransformationEntry entry = new TransformationEntry(UUID.randomUUID(), ownerUuid, miraculous, optionalCustomizationData);
            historyIndex.put(ownerUuid, entry);
            entryById.put(entry.transformationId(), entry);
            setDirty();
        }
    }

    /// Gets all recorded transformation entries for a player/holder.
    public ImmutableSet<TransformationEntry> getHistory(UUID ownerUuid) {
        return ImmutableSet.copyOf(historyIndex.get(ownerUuid));
    }

    /// Gets every unique miraculous holder recorded for this player,
    /// ready to populate your selection screen list.
    public ImmutableSet<Holder<Miraculous>> getRecordedTransformations(UUID ownerUuid) {
        Set<Holder<Miraculous>> miraculousSet = new ObjectOpenHashSet<>();
        for (TransformationEntry transformationEntry : historyIndex.get(ownerUuid)) {
            miraculousSet.add(transformationEntry.miraculous());
        }
        return ImmutableSet.copyOf(miraculousSet);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("Entries", MAP_CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), entryById).getOrThrow());
        return tag;
    }

    private static TransformationHistoryData load(CompoundTag tag, HolderLookup.Provider registries) {
        TransformationHistoryData transformationHistoryData = new TransformationHistoryData();
        if (tag.contains("Entries")) {
            Map<UUID, TransformationEntry> map = MAP_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("Entries")).getOrThrow();
            for (TransformationEntry entry : map.values()) {
                transformationHistoryData.entryById.put(entry.transformationId(), entry);
                transformationHistoryData.historyIndex.put(entry.ownerUuid(), entry);
            }
        }
        return transformationHistoryData;
    }

    public record TransformationEntry(UUID transformationId, UUID ownerUuid, Holder<Miraculous> miraculous, Optional<CustomizationData> customizationData) {
        public static final Codec<TransformationEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                UUIDUtil.CODEC.fieldOf("transformation_id").forGetter(TransformationEntry::transformationId),
                UUIDUtil.CODEC.fieldOf("owner_uuid").forGetter(TransformationEntry::ownerUuid),
                Miraculous.CODEC.fieldOf("miraculous").forGetter(TransformationEntry::miraculous),
                CustomizationData.CODEC.optionalFieldOf("customization_data").forGetter(TransformationEntry::customizationData)).apply(instance, TransformationEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, TransformationEntry> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC,
                TransformationEntry::transformationId,
                UUIDUtil.STREAM_CODEC,
                TransformationEntry::ownerUuid,
                ByteBufCodecs.holderRegistry(MineraculousRegistries.MIRACULOUS),
                TransformationEntry::miraculous,
                ByteBufCodecs.optional(CustomizationData.STREAM_CODEC),
                TransformationEntry::customizationData,
                TransformationEntry::new);
    }
}
