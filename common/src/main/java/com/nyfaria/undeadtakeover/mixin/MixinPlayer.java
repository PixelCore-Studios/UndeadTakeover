package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.config.UndeadTakeoverConfig;
import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.entity.data.MothAttachRemovalGate;
import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import com.nyfaria.undeadtakeover.entity.MothSoul;
import com.nyfaria.undeadtakeover.entity.Shadowed;
import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Unique
    private float undeadtakeover$healthBeforeHurt;

    @Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
    private void undeadtakeover$struggleAgainstGravebeak(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;

        if (player.getVehicle() instanceof Gravebeak gravebeak) {
            cir.setReturnValue(!player.level().isClientSide() && gravebeak.tryBreakFree(player.isShiftKeyDown()));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void undeadtakeover$tickMothShoulderSiphon(CallbackInfo ci) {
        Player player = (Player) (Object) this;

        if (player.level().isClientSide()) {
            return;
        }

        MothAttachState state = Services.MOTH_SHOULDER.get(player);

        if (state.isEmpty()) {
            return;
        }

        int sitTicks = state.sitTicks() + 1;

        if (sitTicks >= UndeadTakeoverConfig.siphonIntervalTicks()) {
            sitTicks = 0;
            MothSoul.siphonMaxHealth(player);
        }

        Services.MOTH_SHOULDER.set(player, state.withSitTicks(sitTicks));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void undeadtakeover$tickSoulDrainParalysis(CallbackInfo ci) {
        Player player = (Player) (Object) this;

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        SoulStolenState state = Services.SOUL_STOLEN.get(player);

        if (state.isDraining() && !(serverLevel.getEntity(state.drainerEntityId()) instanceof Shadowed shadowed && shadowed.isAlive())) {
            state = state.withDrain(0, -1);
            Services.SOUL_STOLEN.set(player, state);
        }

        Shadowed.setParalyzed(player, state.isDraining());
    }

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void undeadtakeover$captureHealthBeforeHurt(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        this.undeadtakeover$healthBeforeHurt = ((Player) (Object) this).getHealth();
    }

    @Redirect(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;removeEntitiesOnShoulder()V"))
    private void undeadtakeover$suppressPrematureMothRemoval(Player player) {
        if (Services.MOTH_SHOULDER.get(player).isEmpty()) {
            ((PlayerShoulderInvoker) player).undeadtakeover$removeEntitiesOnShoulder();
        }
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void undeadtakeover$resolveMothRemoval(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        MothAttachState state = Services.MOTH_SHOULDER.get(player);

        if (state.isEmpty()) {
            return;
        }

        float lost = this.undeadtakeover$healthBeforeHurt - player.getHealth();

        if (lost >= 1.0F && player instanceof MothAttachRemovalGate gate) {
            gate.undeadtakeover$authorizeMothRemoval();
            ((PlayerShoulderInvoker) player).undeadtakeover$removeEntitiesOnShoulder();
        }
    }
}
