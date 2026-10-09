package com.nyfaria.undeadtakeover.platform;

import com.nyfaria.undeadtakeover.init.DataAttachmentsInit;
import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import com.nyfaria.undeadtakeover.platform.services.IMothAttachHelper;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.entity.player.Player;

public class FabricMothAttachHelper implements IMothAttachHelper {

    @Override
    public MothAttachState get(Player player) {
        return player.getAttachedOrElse(DataAttachmentsInit.MOTH_SHOULDER, MothAttachState.NONE);
    }

    @Override
    public void set(Player player, MothAttachState state) {
        player.setAttached(DataAttachmentsInit.MOTH_SHOULDER, state);
    }
}
