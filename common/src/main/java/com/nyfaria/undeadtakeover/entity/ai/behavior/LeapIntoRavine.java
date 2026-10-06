package com.nyfaria.undeadtakeover.entity.ai.behavior;

import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.init.MemoryModuleTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Set;

public class LeapIntoRavine extends ExtendedBehaviour<Gravebeak> {
    private static final double LEAP_RANGE_SQR = 1.8 * 1.8;

    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(2)
            .hasMemory(MemoryModuleTypeInit.RAVINE_POS.get())
            .usesMemory(MemoryModuleType.WALK_TARGET);

    private BlockPos ravine;

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Gravebeak entity) {
        if (entity.isDiving() || !entity.onGround() || !entity.isVehicle()) {
            return false;
        }

        this.ravine = BrainUtil.getMemory(entity, MemoryModuleTypeInit.RAVINE_POS.get());

        if (this.ravine == null) {
            return false;
        }

        double dx = this.ravine.getX() + 0.5 - entity.getX();
        double dz = this.ravine.getZ() + 0.5 - entity.getZ();

        return dx * dx + dz * dz <= LEAP_RANGE_SQR;
    }

    @Override
    protected void start(Gravebeak entity) {
        BrainUtil.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        BrainUtil.clearMemory(entity, MemoryModuleTypeInit.RAVINE_POS.get());
        entity.leapInto(this.ravine);
        this.ravine = null;
    }
}
