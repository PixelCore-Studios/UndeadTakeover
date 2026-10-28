package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.DataAttachmentsInit;
import com.nyfaria.undeadtakeover.config.UndeadTakeoverConfig;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.mixin.SpawnPlacementsInvoker;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.config.ModConfig;

public class UndeadTakeover implements ModInitializer {

    @Override
    public void onInitialize() {

        ConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.COMMON, UndeadTakeoverConfig.SPEC);
        DataAttachmentsInit.init();
        CommonClass.init();
        EntityInit.attributeSuppliers.forEach(p -> FabricDefaultAttributeRegistry.register(p.entityTypeSupplier().get(), p.factory().get().build()));
        EntityInit.spawnPlacements.forEach(UndeadTakeover::registerSpawnPlacement);

        ((FabricBlockEntityType) BlockEntityType.SIGN).addValidBlock(BlockInit.BLOOD_BIRCH_SIGN.get());
        ((FabricBlockEntityType) BlockEntityType.SIGN).addValidBlock(BlockInit.BLOOD_BIRCH_WALL_SIGN.get());
        ((FabricBlockEntityType) BlockEntityType.HANGING_SIGN).addValidBlock(BlockInit.BLOOD_BIRCH_HANGING_SIGN.get());
        ((FabricBlockEntityType) BlockEntityType.HANGING_SIGN).addValidBlock(BlockInit.BLOOD_BIRCH_WALL_HANGING_SIGN.get());
    }

    private static <T extends Mob> void registerSpawnPlacement(EntityInit.SpawnPlacementRegister<T> placement) {
        SpawnPlacementsInvoker.undeadtakeover$register(placement.entityTypeSupplier().get(), placement.placementType(), placement.heightmap(), placement.predicate());
    }
}
