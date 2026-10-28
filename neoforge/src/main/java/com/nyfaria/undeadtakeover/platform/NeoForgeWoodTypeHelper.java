package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.platform.services.IWoodTypeHelper;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class NeoForgeWoodTypeHelper implements IWoodTypeHelper {

    @Override
    public BlockSetType registerBlockSetType(String name) {
        return BlockSetType.register(new BlockSetType(Constants.MODID + ":" + name));
    }

    @Override
    public WoodType registerWoodType(String name, BlockSetType blockSetType) {
        return WoodType.register(new WoodType(Constants.MODID + ":" + name, blockSetType));
    }
}
