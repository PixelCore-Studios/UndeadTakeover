package com.nyfaria.undeadtakeover.entity.ai.behavior;

import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.worldgen.MausoleumInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Set;

public class SetRandomAirWalkTarget extends ExtendedBehaviour<GraveTerror> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(1).usesMemory(MemoryModuleType.WALK_TARGET);

    private final float speedModifier;

    public SetRandomAirWalkTarget(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, GraveTerror entity) {
        return entity.getMausoleum() != null;
    }

    @Override
    protected void start(GraveTerror entity) {
        MausoleumInstance mausoleum = entity.getMausoleum();
        BoundingBox box = mausoleum.box();

        int x = Mth.nextInt(entity.getRandom(), box.minX() + 2, box.maxX() - 2);
        int y = Mth.nextInt(entity.getRandom(), mausoleum.upperHalfMinY(), box.maxY() - 2);
        int z = Mth.nextInt(entity.getRandom(), box.minZ() + 2, box.maxZ() - 2);

        BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPos(x, y, z), this.speedModifier, 1));
    }
}
