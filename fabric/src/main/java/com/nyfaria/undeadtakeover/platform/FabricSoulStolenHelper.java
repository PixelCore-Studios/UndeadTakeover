package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import com.nyfaria.undeadtakeover.init.DataAttachmentsInit;
import com.nyfaria.undeadtakeover.platform.services.ISoulStolenHelper;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.entity.player.Player;

public class FabricSoulStolenHelper implements ISoulStolenHelper {

    @Override
    public SoulStolenState get(Player player) {
        return  player.getAttachedOrElse(DataAttachmentsInit.SOUL_STOLEN, SoulStolenState.NONE);
    }

    @Override
    public void set(Player player, SoulStolenState state) {
         player.setAttached(DataAttachmentsInit.SOUL_STOLEN, state);
    }
}
