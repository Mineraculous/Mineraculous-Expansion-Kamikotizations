package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.reversion.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.IntrinsicReversionEntry;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

public record RestoreWeatherReversionEntry(UUID cause, ResourceKey<Level> dimension, int clearTime, int weatherTime, boolean raining, boolean thundering) implements IntrinsicReversionEntry<RestoreWeatherReversionEntry> {

    private static final MapCodec<RestoreWeatherReversionEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("cause").forGetter(RestoreWeatherReversionEntry::cause),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(RestoreWeatherReversionEntry::dimension),
            Codec.INT.fieldOf("clear_time").forGetter(RestoreWeatherReversionEntry::clearTime),
            Codec.INT.fieldOf("weather_time").forGetter(RestoreWeatherReversionEntry::weatherTime),
            Codec.BOOL.fieldOf("raining").forGetter(RestoreWeatherReversionEntry::raining),
            Codec.BOOL.fieldOf("thundering").forGetter(RestoreWeatherReversionEntry::thundering)).apply(instance, RestoreWeatherReversionEntry::new));
    private static final StreamCodec<RegistryFriendlyByteBuf, RestoreWeatherReversionEntry> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, RestoreWeatherReversionEntry::cause,
            ResourceKey.streamCodec(Registries.DIMENSION), RestoreWeatherReversionEntry::dimension,
            ByteBufCodecs.VAR_INT, RestoreWeatherReversionEntry::clearTime,
            ByteBufCodecs.VAR_INT, RestoreWeatherReversionEntry::weatherTime,
            ByteBufCodecs.BOOL, RestoreWeatherReversionEntry::raining,
            ByteBufCodecs.BOOL, RestoreWeatherReversionEntry::thundering,
            RestoreWeatherReversionEntry::new);
    public static final Type<RestoreWeatherReversionEntry> TYPE = new Type<>(CODEC, STREAM_CODEC);
    public static RestoreWeatherReversionEntry of(UUID cause, ServerLevel level) {
        ServerLevelData data = (ServerLevelData) level.getLevelData();
        return new RestoreWeatherReversionEntry(cause, level.dimension(), data.getClearWeatherTime(), data.getRainTime(), data.isRaining(), data.isThundering());
    }

    @Override
    public Type<RestoreWeatherReversionEntry> type() {
        return MineraculousKamikotizationsReversionEntryTypes.RESTORE_WEATHER.get();
    }

    @Override
    public boolean doRevert(ServerLevel level) {
        level.setWeatherParameters(clearTime, weatherTime, raining, thundering);
        return true;
    }
}
