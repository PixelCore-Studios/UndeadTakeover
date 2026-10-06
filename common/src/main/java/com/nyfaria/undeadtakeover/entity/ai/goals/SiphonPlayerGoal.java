package com.nyfaria.undeadtakeover.entity.ai.goals;

import com.nyfaria.undeadtakeover.entity.MothSoul;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SiphonPlayerGoal extends Goal {
    private static final double SEARCH_RADIUS = 12.0;
    private static final double PERCH_RANGE_SQR = 1.2 * 1.2;

    private final MothSoul moth;
    private Player target;
    private int giveUpCooldown;

    public SiphonPlayerGoal(MothSoul moth) {
        this.moth = moth;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.giveUpCooldown > 0) {
            this.giveUpCooldown--;
            return false;
        }

        if (this.moth.getRandom().nextInt(200) != 0) {
            return false;
        }

        Player nearest = this.moth.level().getNearestPlayer(this.moth.getX(), this.moth.getY(), this.moth.getZ(), SEARCH_RADIUS,
                entity -> entity instanceof ServerPlayer player && isPerchable(player) && this.moth.getSensing().hasLineOfSight(player));

        if (nearest == null) {
            return false;
        }

        this.target = nearest;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && isPerchable(this.target)
                && this.moth.distanceToSqr(this.target) <= SEARCH_RADIUS * SEARCH_RADIUS;
    }

    @Override
    public void stop() {
        this.target = null;
        this.giveUpCooldown = 100 + this.moth.getRandom().nextInt(200);
    }

    @Override
    public void tick() {
        this.moth.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (this.moth.distanceToSqr(this.target) <= PERCH_RANGE_SQR) {
            if (this.target instanceof ServerPlayer serverPlayer && this.moth.perchOnShoulder(serverPlayer)) {
                return;
            }
        }

        this.moth.getNavigation().moveTo(this.target, 1.0);
    }

    private static boolean isPerchable(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer) || player.isSpectator()) {
            return false;
        }

        return serverPlayer.getShoulderEntityLeft().isEmpty() || serverPlayer.getShoulderEntityRight().isEmpty();
    }
}
