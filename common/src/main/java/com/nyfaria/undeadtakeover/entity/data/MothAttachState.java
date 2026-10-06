package com.nyfaria.undeadtakeover.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MothAttachState(boolean left, boolean right, int sitTicks) {
    public static final MothAttachState NONE = new MothAttachState(false, false, 0);

    public static final MapCodec<MothAttachState> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("left").forGetter(MothAttachState::left),
            Codec.BOOL.fieldOf("right").forGetter(MothAttachState::right),
            Codec.INT.fieldOf("sit_ticks").forGetter(MothAttachState::sitTicks)
    ).apply(instance, MothAttachState::new));
    public static final Codec<MothAttachState> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<ByteBuf, MothAttachState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, MothAttachState::left,
            ByteBufCodecs.BOOL, MothAttachState::right,
            ByteBufCodecs.VAR_INT, MothAttachState::sitTicks,
            MothAttachState::new
    );

    public boolean isEmpty() {
        return !this.left && !this.right;
    }

    public MothAttachState withLeft(boolean value) {
        return new MothAttachState(value, this.right, this.sitTicks);
    }

    public MothAttachState withRight(boolean value) {
        return new MothAttachState(this.left, value, this.sitTicks);
    }

    public MothAttachState withSitTicks(int value) {
        return new MothAttachState(this.left, this.right, value);
    }
}
