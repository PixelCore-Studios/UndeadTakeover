package com.nyfaria.undeadtakeover.entity.ai.behavior;

import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.worldgen.MausoleumInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;

import java.util.List;
import java.util.Set;

public class SummonUndeadBehaviour extends ExtendedBehaviour<GraveTerror> {
    private static final List<EntityType<? extends Mob>> SUMMON_POOL = List.of(
            EntityType.ZOMBIE, EntityType.HUSK, EntityType.SKELETON, EntityInit.DECAYING_BODY.get()
    );

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return Set.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GraveTerror entity) {
        return entity.getMausoleum() != null;
    }

    @Override
    protected void start(GraveTerror entity) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        MausoleumInstance mausoleum = entity.getMausoleum();
        BoundingBox box = mausoleum.box();
        int count = 3 + entity.getRandom().nextInt(2);
        int spanX = Math.max(1, box.maxX() - box.minX() - 4);
        int spanZ = Math.max(1, box.maxZ() - box.minZ() - 4);

        for (int i = 0; i < count; i++) {
            EntityType<? extends Mob> type = SUMMON_POOL.get(entity.getRandom().nextInt(SUMMON_POOL.size()));
            Mob mob = type.create(serverLevel, EntitySpawnReason.MOB_SUMMONED);

            if (mob == null) {
                continue;
            }

            double x = box.minX() + 2 + entity.getRandom().nextInt(spanX);
            double y = box.minY() + 1;
            double z = box.minZ() + 2 + entity.getRandom().nextInt(spanZ);

            mob.snapTo(x, y, z, entity.getRandom().nextFloat() * 360.0F, 0.0F);
            mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.MOB_SUMMONED, null);
            serverLevel.addFreshEntity(mob);
        }
    }
}
