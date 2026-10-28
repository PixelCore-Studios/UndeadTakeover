package com.nyfaria.undeadtakeover.datagen;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.block.RevenantAnchorBlock;
import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.ItemInit;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Constants.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createAirLikeBlock(BlockInit.WAVE_BLOCK.get(), Items.STRUCTURE_VOID);
        itemModels.generateFlatItem(BlockInit.WAVE_BLOCK.get().asItem(), Items.STRUCTURE_VOID, ModelTemplates.FLAT_ITEM);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockInit.BLOOD.get(), BlockModelGenerators.plainVariant(
                ModelTemplates.PARTICLE_ONLY.create(BlockInit.BLOOD.get(), TextureMapping.particle(TextureMapping.getBlockTexture(BlockInit.BLOOD.get(), "_still")), blockModels.modelOutput))));
        itemModels.generateFlatItem(ItemInit.BLOOD_BUCKET.get(), ModelTemplates.FLAT_ITEM);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(BlockInit.REVENANT_PORTAL.get())
                .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_AXIS)
                        .select(Direction.Axis.X, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(BlockInit.REVENANT_PORTAL.get(), "_ns")))
                        .select(Direction.Axis.Z, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(BlockInit.REVENANT_PORTAL.get(), "_ew")))));

        Identifier[] anchorModels = new Identifier[5];
        for (int charge = 0; charge < anchorModels.length; charge++) {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(BlockInit.REVENANT_ANCHOR.get(), "_bottom"))
                    .put(TextureSlot.TOP, TextureMapping.getBlockTexture(BlockInit.REVENANT_ANCHOR.get(), charge == 0 ? "_top_off" : "_top"))
                    .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(BlockInit.REVENANT_ANCHOR.get(), "_side" + charge));
            anchorModels[charge] = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(BlockInit.REVENANT_ANCHOR.get(), "_" + charge, mapping, blockModels.modelOutput);
        }
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(BlockInit.REVENANT_ANCHOR.get())
                .with(PropertyDispatch.initial(RevenantAnchorBlock.CHARGE).generate(charge -> BlockModelGenerators.plainVariant(anchorModels[charge]))));
        blockModels.registerSimpleItemModel(BlockInit.REVENANT_ANCHOR.get(), anchorModels[0]);

        blockModels.createTrivialBlock(BlockInit.BLOOD_BIRCH_LEAVES.get(), TexturedModel.LEAVES);
        blockModels.createCrossBlockWithDefaultItem(BlockInit.BLOOD_BIRCH_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        blockModels.woodProvider(BlockInit.BLOOD_BIRCH_LOG.get())
                .logWithHorizontal(BlockInit.BLOOD_BIRCH_LOG.get())
                .wood(BlockInit.BLOOD_BIRCH_WOOD.get());
        blockModels.woodProvider(BlockInit.BLOOD_BIRCH_STRIPPED_LOG.get())
                .logWithHorizontal(BlockInit.BLOOD_BIRCH_STRIPPED_LOG.get())
                .wood(BlockInit.BLOOD_BIRCH_STRIPPED_WOOD.get());
        blockModels.createHangingSign(BlockInit.BLOOD_BIRCH_STRIPPED_LOG.get(), BlockInit.BLOOD_BIRCH_HANGING_SIGN.get(), BlockInit.BLOOD_BIRCH_WALL_HANGING_SIGN.get());

        BlockFamily bloodBirchFamily = new BlockFamily.Builder(BlockInit.BLOOD_BIRCH_PLANKS.get())
                .door(BlockInit.BLOOD_BIRCH_DOOR.get())
                .trapdoor(BlockInit.BLOOD_BIRCH_TRAPDOOR.get())
                .button(BlockInit.BLOOD_BIRCH_BUTTON.get())
                .pressurePlate(BlockInit.BLOOD_BIRCH_PRESSURE_PLATE.get())
                .stairs(BlockInit.BLOOD_BIRCH_STAIRS.get())
                .slab(BlockInit.BLOOD_BIRCH_SLAB.get())
                .fence(BlockInit.BLOOD_BIRCH_FENCE.get())
                .fenceGate(BlockInit.BLOOD_BIRCH_FENCE_GATE.get())
                .sign(BlockInit.BLOOD_BIRCH_SIGN.get(), BlockInit.BLOOD_BIRCH_WALL_SIGN.get())
                .getFamily();
        blockModels.family(BlockInit.BLOOD_BIRCH_PLANKS.get()).generateFor(bloodBirchFamily);

        itemModels.generateFlatItem(BlockInit.BLOOD_BIRCH_BOAT_ITEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BlockInit.BLOOD_BIRCH_CHEST_BOAT_ITEM.get(), ModelTemplates.FLAT_ITEM);
    }

    protected void simpleGeneratedModel(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    protected void simpleHandHeldModel(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
