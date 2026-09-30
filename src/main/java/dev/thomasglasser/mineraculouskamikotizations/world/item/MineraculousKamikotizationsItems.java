package dev.thomasglasser.mineraculouskamikotizations.world.item;

import dev.thomasglasser.mineraculous.api.core.component.MineraculousDataComponents;
import dev.thomasglasser.mineraculous.impl.world.item.component.Active;
import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.core.component.MineraculousKamikotizationsDataComponents;
import dev.thomasglasser.tommylib.api.registration.ItemHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.Supplier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.NeoForgeMod;

public class MineraculousKamikotizationsItems {
    private static final Registrar.Items ITEMS = Registrar.createItems(MineraculousKamikotizations.MOD_ID);

    // Parasols
    private static final Supplier<Item.Properties> PARASOL_PROPERTIES = () -> new Item.Properties().stacksTo(1).component(MineraculousDataComponents.ACTIVE, Active.DEFAULT);
    public static final Map<DyeColor, ItemHolder<ParasolItem>> PARASOLS = parasols();
    public static final ItemHolder<WeatherControlParasolItem> WEATHER_CONTROL_PARASOL = ITEMS.register("weather_control_parasol", () -> new WeatherControlParasolItem(PARASOL_PROPERTIES.get()
            .component(MineraculousKamikotizationsDataComponents.WEATHER_CONTROL_PARASOL_MODE.get(), WeatherControlParasolItem.Mode.WIND)
            .attributes(ItemAttributeModifiers.builder()
                    .add(NeoForgeMod.CREATIVE_FLIGHT, new AttributeModifier(MineraculousKamikotizations.modLoc("parasol_flight"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ANY)
                    .build())));

    private static SortedMap<DyeColor, ItemHolder<ParasolItem>> parasols() {
        SortedMap<DyeColor, ItemHolder<ParasolItem>> parasols = new Reference2ObjectLinkedOpenHashMap<>();
        for (DyeColor color : DyeColor.values()) {
            parasols.put(color, ITEMS.register(color.getName() + "_parasol", () -> new ParasolItem(PARASOL_PROPERTIES.get())));
        }
        return parasols;
    }

    public static void init() {}
}
