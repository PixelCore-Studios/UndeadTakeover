package com.nyfaria.undeadtakeover.events;

import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.EntityInit;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber
public class CommonModEvents {
    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        EntityInit.attributeSuppliers.forEach(p -> e.put(p.entityTypeSupplier().get(), p.factory().get().build()));
    }

    @SubscribeEvent
    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        EntityInit.spawnPlacements.forEach(placement -> registerSpawnPlacement(event, placement));
    }

    private static <T extends Mob> void registerSpawnPlacement(RegisterSpawnPlacementsEvent event, EntityInit.SpawnPlacementRegister<T> placement) {
        event.register(placement.entityTypeSupplier().get(), placement.placementType(), placement.heightmap(), placement.predicate(), RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void blockEntityValidBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, BlockInit.BLOOD_BIRCH_SIGN.get(), BlockInit.BLOOD_BIRCH_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, BlockInit.BLOOD_BIRCH_HANGING_SIGN.get(), BlockInit.BLOOD_BIRCH_WALL_HANGING_SIGN.get());
    }
}
