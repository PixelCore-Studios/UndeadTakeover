package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.base.BoneSnapshots;
import com.nyfaria.undeadtakeover.client.model.UndeadCatModel;
import com.nyfaria.undeadtakeover.entity.UndeadCat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class UndeadCatRenderer extends ProceduralGeoRenderer<UndeadCat, LivingEntityRenderState> {
    public UndeadCatRenderer(EntityRendererProvider.Context context) {
        super(context, new UndeadCatModel());
    }

    @Override
    protected void animateBones(LivingEntityRenderState renderState, BoneSnapshots snapshots) {
        this.rotateHead(snapshots, renderState, "head");
        this.swingLegs(snapshots, renderState, 0.0F, "back_right_leg", "front_left_leg");
        this.swingLegs(snapshots, renderState, Mth.PI, "back_left_leg", "front_right_leg");
        this.wagTail(snapshots, renderState, "tail");
    }
}
