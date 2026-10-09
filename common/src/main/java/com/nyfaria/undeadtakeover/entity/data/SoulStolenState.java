package com.nyfaria.undeadtakeover.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;
import java.util.UUID;

public record SoulStolenState(boolean faceless, Optional<UUID> stolenBy, int drainTicks, int drainerEntityId) {
    public static final SoulStolenState NONE = new SoulStolenState(false, Optional.empty(), 0, -1);
    public static final int DRAIN_DURATION_TICKS = 200;

    public static final MapCodec<SoulStolenState> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("faceless").forGetter(SoulStolenState::faceless),
            UUIDUtil.CODEC.optionalFieldOf("stolen_by").forGetter(SoulStolenState::stolenBy),
            Codec.INT.fieldOf("drain_ticks").forGetter(SoulStolenState::drainTicks),
            Codec.INT.fieldOf("drainer_entity_id").forGetter(SoulStolenState::drainerEntityId)
    ).apply(instance, SoulStolenState::new));
    public static final Codec<SoulStolenState> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<ByteBuf, SoulStolenState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SoulStolenState::faceless,
            UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional), SoulStolenState::stolenBy,
            ByteBufCodecs.VAR_INT, SoulStolenState::drainTicks,
            ByteBufCodecs.VAR_INT, SoulStolenState::drainerEntityId,
            SoulStolenState::new
    );

    public boolean isDraining() {
        return this.drainerEntityId != -1;
    }

    public SoulStolenState withFaceless(boolean value) {
        return new SoulStolenState(value, this.stolenBy, this.drainTicks, this.drainerEntityId);
    }

    public SoulStolenState withStolenBy(Optional<UUID> value) {
        return new SoulStolenState(this.faceless, value, this.drainTicks, this.drainerEntityId);
    }

    public SoulStolenState withDrain(int drainTicks, int drainerEntityId) {
        return new SoulStolenState(this.faceless, this.stolenBy, drainTicks, drainerEntityId);
    }
}
