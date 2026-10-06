package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.entity.Gravebeak;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public class MixinHumanoidMobRenderer {

    @Inject(method = "extractHumanoidRenderState", at = @At("TAIL"))
    private static void undeadtakeover$standWhenHeldByGravebeak(LivingEntity entity, HumanoidRenderState state, float partialTicks, ItemModelResolver itemModelResolver, CallbackInfo ci) {
        if (entity.getVehicle() instanceof Gravebeak) {
            state.isPassenger = false;
        }
    }
}
