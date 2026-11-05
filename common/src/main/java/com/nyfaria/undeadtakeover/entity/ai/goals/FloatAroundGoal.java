package com.nyfaria.undeadtakeover.entity.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FloatAroundGoal extends Goal {
    private final PathfinderMob mob;

    public FloatAroundGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.getNavigation().isDone() && this.mob.getRandom().nextInt(reducedTickDelay(40)) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getNavigation().isInProgress();
    }

    @Override
    public void start() {
        Vec3 pos = this.findPos();
        if (pos != null) {
            this.mob.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.0);
        }
    }

    private Vec3 findPos() {
        float angle = this.mob.getRandom().nextFloat() * (float) (Math.PI * 2);
        double xDir = Math.cos(angle);
        double zDir = Math.sin(angle);
        Vec3 pos = HoverRandomPos.getPos(this.mob, 10, 4, xDir, zDir, (float) (Math.PI / 2), 3, 1);
        if (pos != null) {
            return pos;
        }
        for (double yOffset : new double[]{-3.0, 1.0}) {
            Vec3 drift = this.mob.position().add(xDir * 6.0, yOffset, zDir * 6.0);
            if (this.mob.level().getBlockState(BlockPos.containing(drift)).isAir()) {
                return drift;
            }
        }
        return null;
    }
}
