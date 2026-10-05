package com.nyfaria.undeadtakeover.entity.ai.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SporadicFlightGoal extends Goal {
    private final PathfinderMob mob;

    public SporadicFlightGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.getNavigation().isDone() || this.mob.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getNavigation().isInProgress() && this.mob.getRandom().nextInt(15) != 0;
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
        Vec3 pos = HoverRandomPos.getPos(this.mob, 8, 6, xDir, zDir, (float) Math.PI, 3, 1);
        return pos != null ? pos : AirAndWaterRandomPos.getPos(this.mob, 8, 4, -2, xDir, zDir, Math.PI);
    }
}
