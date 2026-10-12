package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.DicephalusModel;
import com.nyfaria.undeadtakeover.entity.Dicephalus;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class DicephalusRenderer extends GeoEntityRenderer<Dicephalus, LivingEntityRenderState> {
    public DicephalusRenderer(EntityRendererProvider.Context context) {
        super(context, new DicephalusModel());
    }
}
