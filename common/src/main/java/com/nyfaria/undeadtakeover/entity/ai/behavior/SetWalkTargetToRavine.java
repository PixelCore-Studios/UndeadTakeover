package com.nyfaria.undeadtakeover.entity.ai.behavior;

import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.init.MemoryModuleTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Set;

public class SetWalkTargetToRavine extends ExtendedBehaviour<Gravebeak> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(2)
            .hasMemory(MemoryModuleTypeInit.RAVINE_POS.get())
            .usesMemory(MemoryModuleType.WALK_TARGET);

    private final float speedModifier;

    public SetWalkTargetToRavine(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Gravebeak entity) {
        return !entity.isDiving();
    }

    @Override
    protected void start(Gravebeak entity) {
        BlockPos ravine = BrainUtil.getMemory(entity, MemoryModuleTypeInit.RAVINE_POS.get());

        if (ravine != null) {
            BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(ravine, this.speedModifier, 1));
        }
    }
}
