package com.nyfaria.undeadtakeover.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.undeadtakeover.client.SoulStolenRenderData;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

import java.util.EnumSet;

public class SkeletonHeadLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private static final Identifier SKELETON_TEXTURE = Identifier.withDefaultNamespace("textures/entity/skeleton/skeleton.png");

    private final SkeletonModel<SkeletonRenderState> skeletonModel;
    private final ModelPart headShell;

    public SkeletonHeadLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.skeletonModel = new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON));
        this.headShell = buildHeadShell();
    }

    private static ModelPart buildHeadShell() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot()
            .addOrReplaceChild(
                "head_shell",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, EnumSet.complementOf(EnumSet.of(Direction.NORTH))),
                PartPose.ZERO
            );

        return LayerDefinition.create(mesh, 64, 64).bakeRoot().getChild("head_shell");
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (!((SoulStolenRenderData) (Object) state).undeadtakeover$isFaceless()) {
            return;
        }

        PlayerModel parentModel = this.getParentModel();

        this.skeletonModel.resetPose();

        poseStack.pushPose();
        parentModel.translateToHead(poseStack);

        RenderType skinRenderType = parentModel.renderType(state.skin.body().texturePath());
        submitNodeCollector.submitCustomGeometry(poseStack, skinRenderType, (pose, vertexConsumer) -> {
            poseStack.pushPose();
            poseStack.last().set(pose);
            this.headShell.render(poseStack, vertexConsumer, lightCoords, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        });

        RenderType skullRenderType = this.skeletonModel.renderType(SKELETON_TEXTURE);
        submitNodeCollector.submitCustomGeometry(poseStack, skullRenderType, (pose, vertexConsumer) -> {
            poseStack.pushPose();
            poseStack.last().set(pose);
            this.skeletonModel.head.render(poseStack, vertexConsumer, lightCoords, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        });

        poseStack.popPose();
    }
}
