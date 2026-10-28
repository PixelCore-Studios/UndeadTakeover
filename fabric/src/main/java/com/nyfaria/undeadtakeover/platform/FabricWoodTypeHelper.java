package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.platform.services.IWoodTypeHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class FabricWoodTypeHelper implements IWoodTypeHelper {

    @Override
    public BlockSetType registerBlockSetType(String name) {
        return new BlockSetTypeBuilder().register(Constants.modLoc(name));
    }

    @Override
    public WoodType registerWoodType(String name, BlockSetType blockSetType) {
        return new WoodTypeBuilder().register(Constants.modLoc(name), blockSetType);
    }
}
