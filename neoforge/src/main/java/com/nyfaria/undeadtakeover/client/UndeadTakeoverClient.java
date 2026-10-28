package com.nyfaria.undeadtakeover.client;

import com.nyfaria.undeadtakeover.Constants;
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
import com.nyfaria.undeadtakeover.init.FluidTypeInit;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BuiltInBlockModels;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.resources.model.sprite.Material;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterBlockModelsEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class UndeadTakeoverClient {

    public static final ModelLayerLocation BLOOD_BIRCH_BOAT_LAYER = new ModelLayerLocation(Constants.modLoc("boat/blood_birch"), "main");
    public static final ModelLayerLocation BLOOD_BIRCH_CHEST_BOAT_LAYER = new ModelLayerLocation(Constants.modLoc("chest_boat/blood_birch"), "main");

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.BLOOD_SIPPER.get(), BloodSipperRenderer::new);
        event.registerEntityRenderer(EntityInit.GRAVEBEAK.get(), GravebeakRenderer::new);
        event.registerEntityRenderer(EntityInit.MOTH_SOUL.get(), MothSoulRenderer::new);
        event.registerEntityRenderer(EntityInit.SHADOWED.get(), ShadowedRenderer::new);
        event.registerEntityRenderer(EntityInit.DECAYING_BODY.get(), DecayingBodyRenderer::new);
        event.registerEntityRenderer(EntityInit.DICEPHALUS.get(), DicephalusRenderer::new);
        event.registerEntityRenderer(EntityInit.GRAVE_TERROR.get(), GraveTerrorRenderer::new);
        event.registerEntityRenderer(EntityInit.WIGGLER.get(), WigglerRenderer::new);
        event.registerEntityRenderer(EntityInit.LANKY.get(), LankyRenderer::new);
        event.registerEntityRenderer(EntityInit.SHINGLED.get(), ShingledRenderer::new);
        event.registerEntityRenderer(EntityInit.SKULL_SPIRIT.get(), SkullSpiritRenderer::new);
        event.registerEntityRenderer(EntityInit.UNDEAD_SHEEP.get(), UndeadSheepRenderer::new);
        event.registerEntityRenderer(EntityInit.UNDEAD_CAT.get(), UndeadCatRenderer::new);
        event.registerEntityRenderer(EntityInit.UNDEAD_WOLF.get(), UndeadWolfRenderer::new);
        event.registerEntityRenderer(EntityInit.UNDEAD_AXOLOTL.get(), UndeadAxolotlRenderer::new);
        event.registerEntityRenderer(EntityInit.BLOOD_BIRCH_BOAT.get(), context -> new BoatRenderer(context, BLOOD_BIRCH_BOAT_LAYER));
        event.registerEntityRenderer(EntityInit.BLOOD_BIRCH_CHEST_BOAT.get(), context -> new BoatRenderer(context, BLOOD_BIRCH_CHEST_BOAT_LAYER));
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BLOOD_BIRCH_BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(BLOOD_BIRCH_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
    }

    @SubscribeEvent
    public static void registerBlockModels(RegisterBlockModelsEvent event) {
        BuiltInBlockModels.createSigns(
                event.getBuilder(),
                BlockInit.BLOOD_BIRCH_WOOD_TYPE,
                BlockInit.BLOOD_BIRCH_SIGN.get(),
                BlockInit.BLOOD_BIRCH_WALL_SIGN.get(),
                BlockInit.BLOOD_BIRCH_HANGING_SIGN.get(),
                BlockInit.BLOOD_BIRCH_WALL_HANGING_SIGN.get());
    }

    @SubscribeEvent
    public static void registerFluidModels(RegisterFluidModelsEvent event) {
        event.register(new FluidModel.Unbaked(
                new Material(Constants.modLoc("block/blood_still")),
                new Material(Constants.modLoc("block/blood_flow")),
                null,
                (FluidTintSource) null), FluidInit.BLOOD, FluidInit.FLOWING_BLOOD);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
                fluidFogColor.set(0.33F, 0.02F, 0.03F, 1.0F);
            }

            @Override
            public void modifyFogRender(Camera camera, @Nullable FogEnvironment environment, float renderDistance, float partialTick, FogData fogData) {
                fogData.environmentalStart = 0.0F;
                fogData.environmentalEnd = 6.0F;
            }
        }, FluidTypeInit.BLOOD.get());
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> Sheets.addWoodType(BlockInit.BLOOD_BIRCH_WOOD_TYPE));
    }
}
