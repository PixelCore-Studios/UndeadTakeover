package com.nyfaria.undeadtakeover.datagen;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.init.BlockInit;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Constants.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createAirLikeBlock(BlockInit.WAVE_BLOCK.get(), Items.STRUCTURE_VOID);
        itemModels.generateFlatItem(BlockInit.WAVE_BLOCK.get().asItem(), Items.STRUCTURE_VOID, ModelTemplates.FLAT_ITEM);
    }

    protected void simpleGeneratedModel(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    protected void simpleHandHeldModel(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
