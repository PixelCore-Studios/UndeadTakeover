package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.platform.services.IBlockEntityHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class FabricBlockEntityHelper implements IBlockEntityHelper {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.<T>create(factory::apply, blocks).build();
    }
}
