package com.nyfaria.undeadtakeover.client.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.awcapi.ClientClimberHelper;
import com.nyfaria.undeadtakeover.client.model.WigglerModel;
import com.nyfaria.undeadtakeover.client.renderer.state.WigglerRenderState;
import com.nyfaria.undeadtakeover.entity.Wiggler;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.jspecify.annotations.Nullable;

public class WigglerRenderer extends GeoEntityRenderer<Wiggler, WigglerRenderState> {
    public WigglerRenderer(EntityRendererProvider.Context context) {
        super(context, new WigglerModel());
    }

    @Override
    public WigglerRenderState createRenderState(Wiggler animatable, @Nullable Void relatedObject) {
        return new WigglerRenderState();
    }

    @Override
    public void addRenderData(Wiggler animatable, @Nullable Void relatedObject, WigglerRenderState renderState, float partialTick) {
        ClientClimberHelper.extractClimbingRenderState(animatable, renderState, partialTick);
    }

    @Override
    public void submit(WigglerRenderState renderState, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
        ClientClimberHelper.preRenderClimber(renderState, poseStack);
        super.submit(renderState, poseStack, renderTasks, cameraState);
        ClientClimberHelper.postRenderClimber(renderState, poseStack);
    }
}
