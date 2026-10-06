package com.nyfaria.undeadtakeover.entity.ai.behavior;

import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.init.MemoryModuleTypeInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.base.DelayedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Set;

public class GrabTarget extends DelayedBehaviour<Gravebeak> {
    private static final int GRAB_DELAY = 10;
    private static final double GRAB_RANGE_SQR = 2.5 * 2.5;
    private static final double HOLD_RANGE_SQR = 3.5 * 3.5;

    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(3)
            .hasMemory(MemoryModuleType.ATTACK_TARGET)
            .noMemory(MemoryModuleTypeInit.CARRYING.get())
            .usesMemory(MemoryModuleType.LOOK_TARGET);

    private LivingEntity target;

    public GrabTarget() {
        super(GRAB_DELAY);
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Gravebeak entity) {
        this.target = BrainUtil.getTargetOfEntity(entity);

        return this.target instanceof Player
                && entity.canGrab()
                && !this.target.isPassenger()
                && entity.distanceToSqr(this.target) <= GRAB_RANGE_SQR
                && entity.getSensing().hasLineOfSight(this.target);
    }

    @Override
    protected void start(Gravebeak entity) {
        entity.getNavigation().stop();
        BehaviorUtils.lookAtEntity(entity, this.target);
        entity.triggerAnim(Gravebeak.CONTROLLER, Gravebeak.GRAB_ANIM);
    }

    @Override
    protected void doDelayedAction(Gravebeak entity) {
        if (this.target != null && this.target.isAlive() && !this.target.isPassenger() && entity.distanceToSqr(this.target) <= HOLD_RANGE_SQR) {
            entity.grab(this.target);
        }
        else {
            entity.missGrab();
        }
    }

    @Override
    protected void stop(Gravebeak entity) {
        this.target = null;
    }
}
