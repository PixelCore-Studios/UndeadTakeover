package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.nyfaria.undeadtakeover.client.model.MothSoulModel;
import com.nyfaria.undeadtakeover.entity.MothSoul;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class MothSoulRenderer extends GeoEntityRenderer<MothSoul, LivingEntityRenderState> {
    public MothSoulRenderer(EntityRendererProvider.Context context) {
        super(context, new MothSoulModel());
    }
}
