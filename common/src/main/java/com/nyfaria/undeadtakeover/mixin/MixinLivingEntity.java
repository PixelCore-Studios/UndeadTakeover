package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.init.TagInit;
import com.nyfaria.undeadtakeover.worldgen.GraveTerrorMausoleumManager;
import com.nyfaria.undeadtakeover.worldgen.MausoleumInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(method = "die", at = @At("TAIL"))
    private void undeadtakeover$breakGraveTerrorShield(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!(self.level() instanceof ServerLevel serverLevel) || !self.getType().builtInRegistryHolder().is(TagInit.UNDEAD_MOBS)) {
            return;
        }

        Optional<MausoleumInstance> mausoleum = GraveTerrorMausoleumManager.findContaining(serverLevel, self.blockPosition());

        if (mausoleum.isEmpty()) {
            return;
        }

        AABB box = AABB.of(mausoleum.get().box());
        List<GraveTerror> bosses = serverLevel.getEntitiesOfClass(GraveTerror.class, box);

        for (GraveTerror boss : bosses) {
            boss.onNearbyUndeadKilled();
        }
    }
}
