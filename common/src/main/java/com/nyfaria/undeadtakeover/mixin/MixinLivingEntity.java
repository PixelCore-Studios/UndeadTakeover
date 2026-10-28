package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.block.entity.RevenantAnchorBlockEntity;
import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(method = "die", at = @At("TAIL"))
    private void undeadtakeover$breakGraveTerrorShield(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self.level() instanceof ServerLevel serverLevel) || !self.getType().builtInRegistryHolder().is(TagInit.UNDEAD_MOBS)) {
            return;
        }

        for (GraveTerror boss : serverLevel.getEntitiesOfClass(GraveTerror.class, self.getBoundingBox().inflate(64.0))) {
            boss.onUndeadKilled(self.blockPosition());
        }

        if (damageSource.getEntity() instanceof Player) {
            RevenantAnchorBlockEntity.onUndeadKilled(serverLevel, self.blockPosition());
        }
    }

    @Inject(method = "travelInWater", at = @At("TAIL"))
    private void undeadtakeover$bloodDrag(Vec3 input, double baseGravity, boolean isFalling, double oldY, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.level().getFluidState(self.blockPosition()).is(TagInit.BLOOD) || self.level().getFluidState(BlockPos.containing(self.getX(), self.getEyeY(), self.getZ())).is(TagInit.BLOOD)) {
            self.setDeltaMovement(self.getDeltaMovement().multiply(0.6, 0.75, 0.6));
        }
    }
}
