package dev.thomasglasser.mineraculouskamikotizations.world.level.storage.reversion.entry;

import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.level.storage.reversion.entry.ReversionEntry;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;

public class MineraculousKamikotizationsReversionEntryTypes {
    private static final Registrar<ReversionEntry.Type<?>> TYPES = Registrar.create(MineraculousRegistries.REVERSION_ENTRY_TYPE, MineraculousKamikotizations.MOD_ID);

    public static final ExtendedHolder<ReversionEntry.Type<?>, ReversionEntry.Type<RestoreWeatherReversionEntry>> RESTORE_WEATHER = TYPES.register("restore_weather", () -> RestoreWeatherReversionEntry.TYPE);

    public static void init() {}
}
