package dev.thomasglasser.mineraculouskamikotizations.core.component;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.item.WeatherControlParasolItem;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class MineraculousKamikotizationsDataComponents {
    private static final Registrar.DataComponents DATA_COMPONENTS = Registrar.createDataComponents(Registries.DATA_COMPONENT_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final ExtendedHolder<DataComponentType<?>, DataComponentType<WeatherControlParasolItem.Mode>> WEATHER_CONTROL_PARASOL_MODE = DATA_COMPONENTS.registerSimple("weather_control_parasol_mode", builder -> builder
            .networkSynchronized(WeatherControlParasolItem.Mode.STREAM_CODEC)
            .persistent(WeatherControlParasolItem.Mode.CODEC)
            .cacheEncoding());

    public static void init() {}
}
