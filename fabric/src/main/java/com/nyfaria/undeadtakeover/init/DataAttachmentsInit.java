package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public class DataAttachmentsInit {
    public static final AttachmentType<MothAttachState> MOTH_SHOULDER = AttachmentRegistry.<MothAttachState>builder()
            .initializer(() -> MothAttachState.NONE)
            .persistent(MothAttachState.CODEC)
            .syncWith(MothAttachState.STREAM_CODEC, AttachmentSyncPredicate.all())
            .buildAndRegister(Identifier.fromNamespaceAndPath(Constants.MODID, "moth_shoulder"));

    public static void init() {
    }
}
