package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.block.RevenantAnchorBlock;
import com.nyfaria.undeadtakeover.block.RevenantPortalBlock;
import com.nyfaria.undeadtakeover.block.WaveBlock;
import com.nyfaria.undeadtakeover.block.entity.RevenantAnchorBlockEntity;
import com.nyfaria.undeadtakeover.platform.Services;
import com.nyfaria.undeadtakeover.registration.RegistrationProvider;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;
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

    public static final RegistryObject<Block, LiquidBlock> BLOOD = registerBlockWithoutItem("blood",
            () -> new LiquidBlock(FluidInit.BLOOD.get(), BlockBehaviour.Properties.of()
                    .setId(blockKey("blood"))
                    .mapColor(MapColor.CRIMSON_NYLIUM)
                    .replaceable()
                    .noCollision()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)));

    public static final RegistryObject<Block, RevenantPortalBlock> REVENANT_PORTAL = registerBlockWithoutItem("revenant_portal",
            () -> new RevenantPortalBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("revenant_portal"))
                    .noCollision()
                    .strength(-1.0F, 3600000.0F)
                    .sound(SoundType.GLASS)
                    .lightLevel(state -> 11)
                    .pushReaction(PushReaction.BLOCK)
                    .noLootTable()));

    public static final RegistryObject<Block, RevenantAnchorBlock> REVENANT_ANCHOR = registerBlock("revenant_anchor",
            () -> new RevenantAnchorBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("revenant_anchor"))
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .lightLevel(state -> state.getValue(RevenantAnchorBlock.CHARGE) * 15 / 4)));

    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<RevenantAnchorBlockEntity>> REVENANT_ANCHOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("revenant_anchor",
            () -> Services.BLOCK_ENTITY_TYPES.createType(RevenantAnchorBlockEntity::new, REVENANT_ANCHOR.get()));

    private static final TreeGrower BLOOD_BIRCH_GROWER = new TreeGrower("blood_birch", Optional.empty(), Optional.of(FeatureInit.BLOOD_BIRCH_TREE), Optional.empty());

    public static final RegistryObject<Block, RotatedPillarBlock> BLOOD_BIRCH_LOG = registerBlock("blood_birch_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_log"))
                    .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.SAND : MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, UntintedParticleLeavesBlock> BLOOD_BIRCH_LEAVES = registerBlock("blood_birch_leaves",
            () -> new UntintedParticleLeavesBlock(0.0F, ParticleTypes.CHERRY_LEAVES, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_leaves"))
                    .mapColor(MapColor.PLANT)
                    .strength(0.2F)
                    .randomTicks()
                    .sound(SoundType.GRASS)
                    .noOcclusion()
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state, level, pos) -> false)));

    public static final RegistryObject<Block, SaplingBlock> BLOOD_BIRCH_SAPLING = registerBlock("blood_birch_sapling",
            () -> new SaplingBlock(BLOOD_BIRCH_GROWER, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_sapling"))
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .pushReaction(PushReaction.DESTROY)));

    public static final BlockSetType BLOOD_BIRCH_SET = Services.WOOD_TYPES.registerBlockSetType("blood_birch");
    public static final WoodType BLOOD_BIRCH_WOOD_TYPE = Services.WOOD_TYPES.registerWoodType("blood_birch", BLOOD_BIRCH_SET);

    public static final RegistryObject<Block, RotatedPillarBlock> BLOOD_BIRCH_STRIPPED_LOG = registerBlock("blood_birch_stripped_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_stripped_log"))
                    .mapColor(MapColor.SAND)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, RotatedPillarBlock> BLOOD_BIRCH_WOOD = registerBlock("blood_birch_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_wood"))
                    .mapColor(MapColor.SAND)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, RotatedPillarBlock> BLOOD_BIRCH_STRIPPED_WOOD = registerBlock("blood_birch_stripped_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_stripped_wood"))
                    .mapColor(MapColor.SAND)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, Block> BLOOD_BIRCH_PLANKS = registerBlock("blood_birch_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_planks"))
                    .mapColor(MapColor.SAND)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, StairBlock> BLOOD_BIRCH_STAIRS = registerBlock("blood_birch_stairs",
            () -> new StairBlock(BLOOD_BIRCH_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_stairs"))
                    .mapColor(MapColor.SAND)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, SlabBlock> BLOOD_BIRCH_SLAB = registerBlock("blood_birch_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_slab"))
                    .mapColor(MapColor.SAND)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, FenceBlock> BLOOD_BIRCH_FENCE = registerBlock("blood_birch_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_fence"))
                    .mapColor(MapColor.SAND)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()));

    public static final RegistryObject<Block, FenceGateBlock> BLOOD_BIRCH_FENCE_GATE = registerBlock("blood_birch_fence_gate",
            () -> new FenceGateBlock(BLOOD_BIRCH_WOOD_TYPE, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_fence_gate"))
                    .mapColor(MapColor.SAND)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .ignitedByLava()));

    public static final RegistryObject<Block, DoorBlock> BLOOD_BIRCH_DOOR = registerBlock("blood_birch_door",
            () -> new DoorBlock(BLOOD_BIRCH_SET, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_door"))
                    .mapColor(MapColor.SAND)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0F)
                    .noOcclusion()
                    .ignitedByLava()),
            reg -> () -> new DoubleHighBlockItem(reg.get(), ItemInit.getItemProperties()
                    .setId(ResourceKey.create(Registries.ITEM, Constants.modLoc("blood_birch_door")))
                    .useBlockDescriptionPrefix()));

    public static final RegistryObject<Block, TrapDoorBlock> BLOOD_BIRCH_TRAPDOOR = registerBlock("blood_birch_trapdoor",
            () -> new TrapDoorBlock(BLOOD_BIRCH_SET, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_trapdoor"))
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0F)
                    .noOcclusion()
                    .isValidSpawn((state, level, pos, type) -> false)
                    .ignitedByLava()));

    public static final RegistryObject<Block, ButtonBlock> BLOOD_BIRCH_BUTTON = registerBlock("blood_birch_button",
            () -> new ButtonBlock(BLOOD_BIRCH_SET, 30, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_button"))
                    .noCollision()
                    .strength(0.5F)
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block, PressurePlateBlock> BLOOD_BIRCH_PRESSURE_PLATE = registerBlock("blood_birch_pressure_plate",
            () -> new PressurePlateBlock(BLOOD_BIRCH_SET, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_pressure_plate"))
                    .mapColor(MapColor.SAND)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(0.5F)
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block, StandingSignBlock> BLOOD_BIRCH_SIGN = registerBlockWithoutItem("blood_birch_sign",
            () -> new StandingSignBlock(BLOOD_BIRCH_WOOD_TYPE, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_sign"))
                    .mapColor(MapColor.WOOD)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava()));

    public static final RegistryObject<Block, WallSignBlock> BLOOD_BIRCH_WALL_SIGN = registerBlockWithoutItem("blood_birch_wall_sign",
            () -> new WallSignBlock(BLOOD_BIRCH_WOOD_TYPE, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_wall_sign"))
                    .overrideLootTable(BLOOD_BIRCH_SIGN.get().getLootTable())
                    .overrideDescription(BLOOD_BIRCH_SIGN.get().getDescriptionId())
                    .mapColor(MapColor.WOOD)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava()));

    public static final RegistryObject<Item, SignItem> BLOOD_BIRCH_SIGN_ITEM = ItemInit.ITEMS.register("blood_birch_sign",
            () -> new SignItem(BLOOD_BIRCH_SIGN.get(), BLOOD_BIRCH_WALL_SIGN.get(), ItemInit.getItemProperties()
                    .setId(ResourceKey.create(Registries.ITEM, Constants.modLoc("blood_birch_sign")))
                    .stacksTo(16)));

    public static final RegistryObject<Block, CeilingHangingSignBlock> BLOOD_BIRCH_HANGING_SIGN = registerBlockWithoutItem("blood_birch_hanging_sign",
            () -> new CeilingHangingSignBlock(BLOOD_BIRCH_WOOD_TYPE, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_hanging_sign"))
                    .mapColor(BLOOD_BIRCH_LOG.get().defaultMapColor())
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava()));

    public static final RegistryObject<Block, WallHangingSignBlock> BLOOD_BIRCH_WALL_HANGING_SIGN = registerBlockWithoutItem("blood_birch_wall_hanging_sign",
            () -> new WallHangingSignBlock(BLOOD_BIRCH_WOOD_TYPE, BlockBehaviour.Properties.of()
                    .setId(blockKey("blood_birch_wall_hanging_sign"))
                    .overrideLootTable(BLOOD_BIRCH_HANGING_SIGN.get().getLootTable())
                    .overrideDescription(BLOOD_BIRCH_HANGING_SIGN.get().getDescriptionId())
                    .mapColor(BLOOD_BIRCH_LOG.get().defaultMapColor())
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0F)
                    .ignitedByLava()));

    public static final RegistryObject<Item, HangingSignItem> BLOOD_BIRCH_HANGING_SIGN_ITEM = ItemInit.ITEMS.register("blood_birch_hanging_sign",
            () -> new HangingSignItem(BLOOD_BIRCH_HANGING_SIGN.get(), BLOOD_BIRCH_WALL_HANGING_SIGN.get(), ItemInit.getItemProperties()
                    .setId(ResourceKey.create(Registries.ITEM, Constants.modLoc("blood_birch_hanging_sign")))
                    .stacksTo(16)));

    public static final RegistryObject<Item, Item> BLOOD_BIRCH_BOAT_ITEM = ItemInit.ITEMS.register("blood_birch_boat",
            () -> new BoatItem(EntityInit.BLOOD_BIRCH_BOAT.get(), ItemInit.getItemProperties()
                    .setId(ResourceKey.create(Registries.ITEM, Constants.modLoc("blood_birch_boat")))
                    .stacksTo(1)));

    public static final RegistryObject<Item, Item> BLOOD_BIRCH_CHEST_BOAT_ITEM = ItemInit.ITEMS.register("blood_birch_chest_boat",
            () -> new BoatItem(EntityInit.BLOOD_BIRCH_CHEST_BOAT.get(), ItemInit.getItemProperties()
                    .setId(ResourceKey.create(Registries.ITEM, Constants.modLoc("blood_birch_chest_boat")))
                    .stacksTo(1)));


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
