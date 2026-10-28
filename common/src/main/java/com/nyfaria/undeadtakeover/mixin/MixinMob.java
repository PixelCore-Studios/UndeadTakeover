package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.init.DimensionInit;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MixinMob {

    @Inject(method = "isSunBurnTick", at = @At("HEAD"), cancellable = true)
    private void undeadtakeover$noSunBurnInRevenant(CallbackInfoReturnable<Boolean> cir) {
        if (((Mob) (Object) this).level().dimension() == DimensionInit.THE_REVENANT) {
            cir.setReturnValue(false);
        }
    }
}
