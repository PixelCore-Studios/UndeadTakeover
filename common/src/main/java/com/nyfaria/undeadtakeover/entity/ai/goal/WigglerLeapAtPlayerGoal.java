package com.nyfaria.undeadtakeover.entity.ai.goal;

import com.nyfaria.awcapi.entity.movement.ClimberJumpController;
import com.nyfaria.undeadtakeover.entity.Wiggler;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WigglerLeapAtPlayerGoal extends Goal {
    private static final double MIN_DIST_SQR = 4.0;
    private static final double MAX_DIST_SQR = 36.0;

    private final Wiggler wiggler;
    private LivingEntity target;

    public WigglerLeapAtPlayerGoal(Wiggler wiggler) {
        this.wiggler = wiggler;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.wiggler.getClimberComponent().getGroundSide() == Direction.DOWN) {
            return false;
        }

        LivingEntity target = this.wiggler.getTarget();

        if (target == null || !target.isAlive()) {
            return false;
        }

        double distSqr = this.wiggler.distanceToSqr(target);

        if (distSqr < MIN_DIST_SQR || distSqr > MAX_DIST_SQR) {
            return false;
        }

        this.target = target;
        return this.wiggler.getRandom().nextInt(reducedTickDelay(20)) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        Vec3 dir = new Vec3(
                this.target.getX() - this.wiggler.getX(),
                this.target.getY() + this.target.getEyeHeight() * 0.5 - this.wiggler.getEyeY(),
                this.target.getZ() - this.wiggler.getZ()
        ).normalize();

        if (this.wiggler.getJumpControl() instanceof ClimberJumpController<?> jumpControl) {
            jumpControl.setJumping(dir);
        }
    }
}
