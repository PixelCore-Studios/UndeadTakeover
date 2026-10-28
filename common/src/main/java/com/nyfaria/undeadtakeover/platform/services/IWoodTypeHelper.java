package com.nyfaria.undeadtakeover.platform.services;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public interface IWoodTypeHelper {

    BlockSetType registerBlockSetType(String name);

    WoodType registerWoodType(String name, BlockSetType blockSetType);
}
