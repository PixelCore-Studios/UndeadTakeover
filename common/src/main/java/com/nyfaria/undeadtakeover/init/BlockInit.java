package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.block.WaveBlock;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);

    public static final RegistryObject<Block, WaveBlock> WAVE_BLOCK = registerBlock("wave_block",
            () -> new WaveBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("wave_block"))
                    .mapColor(MapColor.NONE)
                    .strength(-1.0F, 3600000.8F)
                    .noCollision()
                    .noOcclusion()
                    .noLootTable()
                    .noTerrainParticles()));


    public static <T extends Block> RegistryObject<Block,T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties()
                .setId(ResourceKey.create(Registries.ITEM, Constants.modLoc(name)))
                .useBlockDescriptionPrefix()));
    }

    protected static <T extends Block> RegistryObject<Block,T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<Block,T>, Supplier<? extends BlockItem>> item) {
        RegistryObject<Block,T> reg = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }

    public static <T extends Block> RegistryObject<Block,T> registerBlockWithoutItem(String name, Supplier<T> block) {
        var reg = BLOCKS.register(name, block);
        return reg;
    }

    protected static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.modLoc(name));
    }


    public static void loadClass() {
    }
}
