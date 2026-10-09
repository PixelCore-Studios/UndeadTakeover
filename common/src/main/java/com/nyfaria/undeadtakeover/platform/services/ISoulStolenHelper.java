package com.nyfaria.undeadtakeover.platform.services;

import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import net.minecraft.world.entity.player.Player;

public interface ISoulStolenHelper {
    SoulStolenState get(Player player);

    void set(Player player, SoulStolenState state);
}
