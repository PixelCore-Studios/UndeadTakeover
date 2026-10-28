package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.base.BoneSnapshots;
import com.nyfaria.undeadtakeover.client.model.UndeadAxolotlModel;
import com.nyfaria.undeadtakeover.entity.UndeadAxolotl;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class UndeadAxolotlRenderer extends ProceduralGeoRenderer<UndeadAxolotl, LivingEntityRenderState> {
    public UndeadAxolotlRenderer(EntityRendererProvider.Context context) {
        super(context, new UndeadAxolotlModel());
    }

    @Override
    protected void animateBones(LivingEntityRenderState renderState, BoneSnapshots snapshots) {
        float gills = Mth.sin(renderState.ageInTicks * 0.1F) * 0.15F;

        this.rotateHead(snapshots, renderState, "head");
        this.swingLegs(snapshots, renderState, 0.0F, "leg1", "leg4");
        this.swingLegs(snapshots, renderState, Mth.PI, "leg2", "leg3");
        this.wagTail(snapshots, renderState, "tail");
        snapshots.ifPresent("top_gills", snapshot -> snapshot.setRotX(gills));
        snapshots.ifPresent("left_gills", snapshot -> snapshot.setRotY(gills));
        snapshots.ifPresent("right_gills", snapshot -> snapshot.setRotY(-gills));
    }
}
