package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.BloodSipperModel;
import com.nyfaria.undeadtakeover.entity.BloodSipper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class BloodSipperRenderer extends GeoEntityRenderer<BloodSipper, LivingEntityRenderState> {
    public BloodSipperRenderer(EntityRendererProvider.Context context) {
        super(context, new BloodSipperModel());
    }
}
