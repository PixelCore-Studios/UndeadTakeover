package com.nyfaria.undeadtakeover.entity.ai.behavior;

import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;

import java.util.List;
import java.util.Set;

public class SummonUndeadBehaviour extends ExtendedBehaviour<GraveTerror> {
    private static final double PLAYER_PROXIMITY_RANGE = 32.0;
    private static final List<EntityType<? extends Mob>> SUMMON_POOL = List.of(
            EntityType.ZOMBIE, EntityType.HUSK, EntityType.SKELETON, EntityInit.DECAYING_BODY.get()
    );

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return Set.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GraveTerror entity) {
        return entity.isAirPhase()
                && !entity.getWavePositions().isEmpty()
                && level.hasNearbyAlivePlayer(entity.getX(), entity.getY(), entity.getZ(), PLAYER_PROXIMITY_RANGE);
    }

    @Override
    protected void start(GraveTerror entity) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        List<BlockPos> wavePositions = entity.getWavePositions();
        int count = 3 + entity.getRandom().nextInt(2);
        int offset = entity.getRandom().nextInt(wavePositions.size());

        for (int i = 0; i < count; i++) {
            EntityType<? extends Mob> type = SUMMON_POOL.get(entity.getRandom().nextInt(SUMMON_POOL.size()));
            Mob mob = type.create(serverLevel, EntitySpawnReason.MOB_SUMMONED);

            if (mob == null) {
                continue;
            }

            BlockPos pos = wavePositions.get((i + offset) % wavePositions.size());

            mob.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, entity.getRandom().nextFloat() * 360.0F, 0.0F);
            mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
            serverLevel.addFreshEntity(mob);
        }
    }
}
