package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.GravebeakModel;
import com.nyfaria.undeadtakeover.entity.Gravebeak;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class GravebeakRenderer extends GeoEntityRenderer<Gravebeak, LivingEntityRenderState> {
    public GravebeakRenderer(EntityRendererProvider.Context context) {
        super(context, new GravebeakModel());
    }
}
