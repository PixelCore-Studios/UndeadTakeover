package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.client.renderer.BloodSipperRenderer;
import com.nyfaria.undeadtakeover.client.renderer.DecayingBodyRenderer;
import com.nyfaria.undeadtakeover.client.renderer.DicephalusRenderer;
import com.nyfaria.undeadtakeover.client.renderer.GravebeakRenderer;
import com.nyfaria.undeadtakeover.client.renderer.GraveTerrorRenderer;
import com.nyfaria.undeadtakeover.client.renderer.LankyRenderer;
import com.nyfaria.undeadtakeover.client.renderer.MothSoulRenderer;
import com.nyfaria.undeadtakeover.client.renderer.ShadowedRenderer;
import com.nyfaria.undeadtakeover.client.renderer.ShingledRenderer;
import com.nyfaria.undeadtakeover.client.renderer.SkullSpiritRenderer;
import com.nyfaria.undeadtakeover.client.renderer.UndeadAxolotlRenderer;
import com.nyfaria.undeadtakeover.client.renderer.UndeadCatRenderer;
import com.nyfaria.undeadtakeover.client.renderer.UndeadSheepRenderer;
import com.nyfaria.undeadtakeover.client.renderer.UndeadWolfRenderer;
import com.nyfaria.undeadtakeover.client.renderer.WigglerRenderer;
import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.init.FluidInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltInBlockModelsCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.block.BuiltInBlockModels;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.resources.model.sprite.Material;

public class UndeadTakeoverClient implements ClientModInitializer {

    public static final ModelLayerLocation BLOOD_BIRCH_BOAT_LAYER = new ModelLayerLocation(Constants.modLoc("boat/blood_birch"), "main");
    public static final ModelLayerLocation BLOOD_BIRCH_CHEST_BOAT_LAYER = new ModelLayerLocation(Constants.modLoc("chest_boat/blood_birch"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityInit.BLOOD_SIPPER.get(), BloodSipperRenderer::new);
        EntityRendererRegistry.register(EntityInit.GRAVEBEAK.get(), GravebeakRenderer::new);
        EntityRendererRegistry.register(EntityInit.MOTH_SOUL.get(), MothSoulRenderer::new);
        EntityRendererRegistry.register(EntityInit.SHADOWED.get(), ShadowedRenderer::new);
        EntityRendererRegistry.register(EntityInit.DECAYING_BODY.get(), DecayingBodyRenderer::new);
        EntityRendererRegistry.register(EntityInit.DICEPHALUS.get(), DicephalusRenderer::new);
        EntityRendererRegistry.register(EntityInit.GRAVE_TERROR.get(), GraveTerrorRenderer::new);
        EntityRendererRegistry.register(EntityInit.WIGGLER.get(), WigglerRenderer::new);
        EntityRendererRegistry.register(EntityInit.LANKY.get(), LankyRenderer::new);
        EntityRendererRegistry.register(EntityInit.SHINGLED.get(), ShingledRenderer::new);
        EntityRendererRegistry.register(EntityInit.SKULL_SPIRIT.get(), SkullSpiritRenderer::new);
        EntityRendererRegistry.register(EntityInit.UNDEAD_SHEEP.get(), UndeadSheepRenderer::new);
        EntityRendererRegistry.register(EntityInit.UNDEAD_CAT.get(), UndeadCatRenderer::new);
        EntityRendererRegistry.register(EntityInit.UNDEAD_WOLF.get(), UndeadWolfRenderer::new);
        EntityRendererRegistry.register(EntityInit.UNDEAD_AXOLOTL.get(), UndeadAxolotlRenderer::new);
        EntityRendererRegistry.register(EntityInit.BLOOD_BIRCH_BOAT.get(), context -> new BoatRenderer(context, BLOOD_BIRCH_BOAT_LAYER));
        EntityRendererRegistry.register(EntityInit.BLOOD_BIRCH_CHEST_BOAT.get(), context -> new BoatRenderer(context, BLOOD_BIRCH_CHEST_BOAT_LAYER));

        ModelLayerRegistry.registerModelLayer(BLOOD_BIRCH_BOAT_LAYER, BoatModel::createBoatModel);
        ModelLayerRegistry.registerModelLayer(BLOOD_BIRCH_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);

        BuiltInBlockModelsCallback.EVENT.register(builder -> BuiltInBlockModels.createSigns(
                builder,
                BlockInit.BLOOD_BIRCH_WOOD_TYPE,
                BlockInit.BLOOD_BIRCH_SIGN.get(),
                BlockInit.BLOOD_BIRCH_WALL_SIGN.get(),
                BlockInit.BLOOD_BIRCH_HANGING_SIGN.get(),
                BlockInit.BLOOD_BIRCH_WALL_HANGING_SIGN.get()));

        FluidRenderingRegistry.register(FluidInit.BLOOD.get(), FluidInit.FLOWING_BLOOD.get(), new FluidModel.Unbaked(
                new Material(Constants.modLoc("block/blood_still")),
                new Material(Constants.modLoc("block/blood_flow")),
                null,
                null));
    }
}
