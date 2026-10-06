package com.nyfaria.undeadtakeover.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nyfaria.undeadtakeover.client.MothShoulderRenderData;
import com.nyfaria.undeadtakeover.client.renderer.MothSoulRenderer;
import com.nyfaria.undeadtakeover.entity.MothSoul;
import com.nyfaria.undeadtakeover.init.EntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.Level;

public class MothShoulderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final MothSoulRenderer mothRenderer;
    private MothSoul dummyMoth;
    private double age;
    private long lastNanoTime;

    public MothShoulderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.mothRenderer = new MothSoulRenderer(context);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        MothShoulderRenderData data = (MothShoulderRenderData) (Object) state;

        if (!data.undeadtakeover$hasMothOnShoulder(true) && !data.undeadtakeover$hasMothOnShoulder(false)) {
            return;
        }

        MothSoul moth = this.getDummyMoth();

        if (moth == null) {
            return;
        }


        moth.setYRot(180.0F);
        moth.yRotO = 180.0F;
        moth.setYHeadRot(180.0F);
        moth.yHeadRotO = 180.0F;
        moth.yBodyRot = 180.0F;
        moth.yBodyRotO = 180.0F;
        moth.setPerchedForRender(true);

        moth.setPos(state.x, state.y, state.z);
        moth.setOldPosAndRot();

        long now = System.nanoTime();
        if (this.lastNanoTime != 0L) {
            this.age += (now - this.lastNanoTime) / 1.0e9 * 20.0;
        }
        this.lastNanoTime = now;

        int ageTicks = (int) this.age;
        float partialTick = (float) (this.age - ageTicks);
        moth.tickCount = ageTicks;

        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(-state.xRot));
        poseStack.translate(0.0F, 0.4F, 0.0F);
        poseStack.scale(0.6F, 0.6F, 0.6F);

        LivingEntityRenderState renderState = this.mothRenderer.createRenderState(moth, partialTick);
        this.mothRenderer.performRenderPass(renderState, poseStack, submitNodeCollector, new CameraRenderState());

        poseStack.popPose();
    }

    private MothSoul getDummyMoth() {
        Level level = Minecraft.getInstance().level;

        if (level == null) {
            return null;
        }

        if (this.dummyMoth == null || this.dummyMoth.level() != level) {
            this.dummyMoth = new MothSoul(EntityInit.MOTH_SOUL.get(), level);
        }

        return this.dummyMoth;
    }
}
