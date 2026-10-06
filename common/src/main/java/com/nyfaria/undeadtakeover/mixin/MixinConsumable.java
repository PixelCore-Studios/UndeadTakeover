package com.nyfaria.undeadtakeover.mixin;

import com.nyfaria.undeadtakeover.entity.MothSoul;
import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Consumable.class)
public class MixinConsumable {

    @Inject(method = "onConsume", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    private void undeadtakeover$cureOnGoldenFood(Level level, LivingEntity user, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (!level.isClientSide() && user instanceof Player player && stack.is(TagInit.HEALTH_RESTORE_FOODS)) {
            MothSoul.cureMaxHealth(player);
        }
    }
}
