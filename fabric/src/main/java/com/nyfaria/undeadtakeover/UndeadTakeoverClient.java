package com.nyfaria.undeadtakeover;

import com.nyfaria.undeadtakeover.client.renderer.BloodSipperRenderer;
import com.nyfaria.undeadtakeover.client.renderer.DecayingBodyRenderer;
import com.nyfaria.undeadtakeover.client.renderer.GravebeakRenderer;
import com.nyfaria.undeadtakeover.client.renderer.MothSoulRenderer;
import com.nyfaria.undeadtakeover.client.renderer.ShadowedRenderer;
import com.nyfaria.undeadtakeover.init.EntityInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class UndeadTakeoverClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityInit.BLOOD_SIPPER.get(), BloodSipperRenderer::new);
        EntityRendererRegistry.register(EntityInit.GRAVEBEAK.get(), GravebeakRenderer::new);
        EntityRendererRegistry.register(EntityInit.MOTH_SOUL.get(), MothSoulRenderer::new);
        EntityRendererRegistry.register(EntityInit.SHADOWED.get(), ShadowedRenderer::new);
        EntityRendererRegistry.register(EntityInit.DECAYING_BODY.get(), DecayingBodyRenderer::new);
    }
}
