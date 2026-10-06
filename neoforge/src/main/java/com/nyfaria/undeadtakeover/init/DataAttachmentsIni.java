package com.nyfaria.undeadtakeover.init;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.entity.data.MothAttachState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DataAttachmentsIni {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Constants.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MothAttachState>> MOTH_SHOULDER =
            ATTACHMENT_TYPES.register("moth_shoulder", () -> AttachmentType.builder(() -> MothAttachState.NONE)
                    .serialize(MothAttachState.MAP_CODEC)
                    .sync(MothAttachState.STREAM_CODEC)
                    .build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
