package com.nyfaria.undeadtakeover.datagen;

import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.flag.FeatureFlags;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.planksFromLogs(BlockInit.BLOOD_BIRCH_PLANKS.get(), TagInit.BLOOD_BIRCH_LOGS_ITEM, 4);
        this.woodFromLogs(BlockInit.BLOOD_BIRCH_WOOD.get(), BlockInit.BLOOD_BIRCH_LOG.get());
        this.woodFromLogs(BlockInit.BLOOD_BIRCH_STRIPPED_WOOD.get(), BlockInit.BLOOD_BIRCH_STRIPPED_LOG.get());
        this.woodenBoat(BlockInit.BLOOD_BIRCH_BOAT_ITEM.get(), BlockInit.BLOOD_BIRCH_PLANKS.get());
        this.chestBoat(BlockInit.BLOOD_BIRCH_CHEST_BOAT_ITEM.get(), BlockInit.BLOOD_BIRCH_BOAT_ITEM.get());
        this.hangingSign(BlockInit.BLOOD_BIRCH_HANGING_SIGN_ITEM.get(), BlockInit.BLOOD_BIRCH_STRIPPED_LOG.get());

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
        this.generateRecipes(bloodBirchFamily, FeatureFlags.VANILLA_SET);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Undead Takeover Recipes";
        }
    }
}
