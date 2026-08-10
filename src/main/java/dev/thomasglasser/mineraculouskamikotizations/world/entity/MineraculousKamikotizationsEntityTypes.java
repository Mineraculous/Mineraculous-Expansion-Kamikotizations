package dev.thomasglasser.mineraculouskamikotizations.world.entity;

import dev.thomasglasser.mineraculouskamikotizations.MineraculousKamikotizations;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.animal.Pigeon;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.projectile.IceCharge;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting.RevertingIceCharge;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting.RevertingLightningBolt;
import dev.thomasglasser.mineraculouskamikotizations.world.entity.reverting.RevertingWindCharge;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MineraculousKamikotizationsEntityTypes {
    private static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(MineraculousKamikotizations.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<IceCharge>> ICE_CHARGE = ENTITY_TYPES.register("ice_charge", IceCharge::new, MobCategory.MISC, builder -> builder
            .sized(0.3125F, 0.3125F));

    // Animals
    public static final DeferredHolder<EntityType<?>, EntityType<Pigeon>> PIGEON = ENTITY_TYPES.register("pigeon", Pigeon::new, MobCategory.CREATURE, builder -> builder
            .sized(0.3325F, 0.3325F));

    // Grief Tracking
    public static final DeferredHolder<EntityType<?>, EntityType<RevertingIceCharge>> REVERTING_ICE_CHARGE = ENTITY_TYPES.register("reverting_ice_charge", RevertingIceCharge::new, MobCategory.MISC, builder -> builder
            .sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<RevertingLightningBolt>> REVERTING_LIGHTNING_BOLT = ENTITY_TYPES.register("reverting_lightning_bolt", RevertingLightningBolt::new, MobCategory.MISC, builder -> builder
            .noSave()
            .sized(0.0F, 0.0F));
    public static final DeferredHolder<EntityType<?>, EntityType<RevertingWindCharge>> REVERTING_WIND_CHARGE = ENTITY_TYPES.register("reverting_wind_charge", RevertingWindCharge::new, MobCategory.MISC, builder -> builder
            .sized(0.3125F, 0.3125F));

    public static void init() {}
}
