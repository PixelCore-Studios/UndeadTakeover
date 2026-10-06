package com.nyfaria.undeadtakeover.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerShoulderInvoker {
    @Invoker("removeEntitiesOnShoulder")
    void undeadtakeover$removeEntitiesOnShoulder();
}
