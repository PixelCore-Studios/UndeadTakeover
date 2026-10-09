package com.nyfaria.undeadtakeover.client;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.client.renderer.BloodSipperRenderer;
import com.nyfaria.undeadtakeover.client.renderer.DecayingBodyRenderer;
import com.nyfaria.undeadtakeover.client.renderer.GravebeakRenderer;
import com.nyfaria.undeadtakeover.client.renderer.MothSoulRenderer;
import com.nyfaria.undeadtakeover.client.renderer.ShadowedRenderer;
import com.nyfaria.undeadtakeover.init.EntityInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class UndeadTakeoverClient {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.BLOOD_SIPPER.get(), BloodSipperRenderer::new);
        event.registerEntityRenderer(EntityInit.GRAVEBEAK.get(), GravebeakRenderer::new);
        event.registerEntityRenderer(EntityInit.MOTH_SOUL.get(), MothSoulRenderer::new);
        event.registerEntityRenderer(EntityInit.SHADOWED.get(), ShadowedRenderer::new);
        event.registerEntityRenderer(EntityInit.DECAYING_BODY.get(), DecayingBodyRenderer::new);
    }
}
