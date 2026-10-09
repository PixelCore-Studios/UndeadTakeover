package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.DecayingBodyModel;
import com.nyfaria.undeadtakeover.entity.DecayingBody;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class DecayingBodyRenderer extends GeoEntityRenderer<DecayingBody, LivingEntityRenderState> {
    public DecayingBodyRenderer(EntityRendererProvider.Context context) {
        super(context, new DecayingBodyModel());
    }
}
