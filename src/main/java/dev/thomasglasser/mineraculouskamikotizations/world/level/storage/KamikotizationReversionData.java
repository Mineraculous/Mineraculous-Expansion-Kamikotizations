package dev.thomasglasser.mineraculouskamikotizations.world.level.storage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class KamikotizationReversionData extends SavedData {
    public static final String FILE_ID = "kamikotization_reversion";

    private boolean weatherModified = false;

    public static KamikotizationReversionData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(KamikotizationReversionData.factory(), KamikotizationReversionData.FILE_ID);
    }

    public static Factory<KamikotizationReversionData> factory() {
        return new Factory<>(KamikotizationReversionData::new, (p_294039_, p_324123_) -> load(p_294039_), DataFixTypes.LEVEL);
    }

    public boolean wasWeatherModified() {
        return weatherModified;
    }

    public void setWeatherModified(boolean weatherModified) {
        this.weatherModified = weatherModified;
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("WeatherModified", weatherModified);
        return tag;
    }

    public static KamikotizationReversionData load(CompoundTag tag) {
        KamikotizationReversionData data = new KamikotizationReversionData();
        data.weatherModified = tag.getBoolean("WeatherModified");
        return data;
    }
}
