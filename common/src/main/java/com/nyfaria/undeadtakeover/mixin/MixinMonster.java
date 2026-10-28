package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.init.DimensionInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MixinMonster {

    @Inject(method = "isDarkEnoughToSpawn", at = @At("HEAD"), cancellable = true)
    private static void undeadtakeover$ignoreSkyLightInRevenant(ServerLevelAccessor level, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (level.getLevel().dimension() == DimensionInit.THE_REVENANT) {
            cir.setReturnValue(level.getBrightness(LightLayer.BLOCK, pos) <= level.dimensionType().monsterSpawnBlockLightLimit());
        }
    }

    @Inject(method = "getWalkTargetValue", at = @At("HEAD"), cancellable = true)
    private void undeadtakeover$ignoreSkyLightWalkTarget(BlockPos pos, LevelReader level, CallbackInfoReturnable<Float> cir) {
        Monster self = (Monster) (Object) this;

        if (self.level().dimension() == DimensionInit.THE_REVENANT) {
            cir.setReturnValue(-level.getBrightness(LightLayer.BLOCK, pos) / 15.0F);
        }
    }
}
