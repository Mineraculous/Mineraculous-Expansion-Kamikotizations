package dev.thomasglasser.mineraculouskamikotizations.world.level.storage;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.mineraculous.api.core.customization.CustomizationData;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class TransformationHistoryData extends SavedData {
    private static final String FILE_ID = "transformation_history";
    private static final Codec<Set<TransformationEntry>> SET_CODEC = TransformationEntry.CODEC.listOf().xmap(ImmutableSet::copyOf, List::copyOf);

    private final Set<TransformationEntry> entries = new ObjectOpenHashSet<>();

    public static TransformationHistoryData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(new Factory<>(TransformationHistoryData::new, TransformationHistoryData::load, DataFixTypes.LEVEL), FILE_ID);
    }

    /// Records a transformation event if an identical one doesn't already exist.
    public void recordTransformation(Holder<Miraculous> miraculous, @Nullable CustomizationData customizationData) {
        if (entries.add(new TransformationEntry(miraculous, Optional.ofNullable(customizationData))))
            setDirty();
    }

    public ImmutableSet<TransformationEntry> getAllEntries() {
        return ImmutableSet.copyOf(entries);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        SET_CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), entries)
                .resultOrPartial(MineraculousKamikotizations.LOGGER::error)
                .ifPresent(encoded -> tag.put("Entries", encoded));
        return tag;
    }

    private static TransformationHistoryData load(CompoundTag tag, HolderLookup.Provider registries) {
        TransformationHistoryData data = new TransformationHistoryData();
        if (tag.contains("Entries"))
            SET_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("Entries"))
                    .resultOrPartial(MineraculousKamikotizations.LOGGER::error)
                    .ifPresent(data.entries::addAll);
        return data;
    }

    public record TransformationEntry(Holder<Miraculous> miraculous, Optional<CustomizationData> customizationData) {
        public static final Codec<TransformationEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Miraculous.CODEC.fieldOf("miraculous").forGetter(TransformationEntry::miraculous),
                CustomizationData.CODEC.optionalFieldOf("customization_data").forGetter(TransformationEntry::customizationData)).apply(instance, TransformationEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, TransformationEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(MineraculousRegistries.MIRACULOUS), TransformationEntry::miraculous,
                ByteBufCodecs.optional(CustomizationData.STREAM_CODEC), TransformationEntry::customizationData,
                TransformationEntry::new);

        public Component getName() {
            return customizationData.flatMap(data -> data.nameSetting().map(data.settings()::get))
                    .filter(name -> !name.isEmpty())
                    .map(name -> Component.literal(name).withStyle(style -> style.withColor(miraculous.value().color())))
                    .orElseGet(() -> Miraculous.createMiraculousStack(miraculous).getHoverName().copy().withStyle(style -> style.withColor(miraculous.value().color()).withObfuscated(true)));
        }
    }
}
