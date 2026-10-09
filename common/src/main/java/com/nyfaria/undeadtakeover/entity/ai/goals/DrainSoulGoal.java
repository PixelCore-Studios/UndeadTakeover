package com.nyfaria.undeadtakeover.entity.ai.goals;

import com.nyfaria.undeadtakeover.entity.Shadowed;
import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.Optional;

public class DrainSoulGoal extends Goal {
    private static final double SEARCH_RADIUS = 16.0;
    private static final double DRAIN_RANGE_SQR = 10.0 * 10.0;
    private static final float TURN_SPEED = 10.0F;
    private static final float FACING_YAW_TOLERANCE = 10.0F;
    private static final float FACING_PITCH_TOLERANCE = 15.0F;

    private final Shadowed shadowed;
    private Player target;
    private int giveUpCooldown;

    public DrainSoulGoal(Shadowed shadowed) {
        this.shadowed = shadowed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.giveUpCooldown > 0) {
            this.giveUpCooldown--;
            return false;
        }

        if (this.shadowed.getRandom().nextInt(100) != 0) {
            return false;
        }

        Player nearest = this.shadowed.level().getNearestPlayer(this.shadowed.getX(), this.shadowed.getY(), this.shadowed.getZ(), SEARCH_RADIUS,
                entity -> entity instanceof ServerPlayer player && isFreshTarget(player) && this.shadowed.getSensing().hasLineOfSight(player));

        if (nearest == null) {
            return false;
        }

        this.target = nearest;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!(this.target instanceof ServerPlayer serverPlayer) || !this.target.isAlive() || this.target.isSpectator()) {
            return false;
        }

        SoulStolenState state = Services.SOUL_STOLEN.get(serverPlayer);

        if (state.faceless() || (state.isDraining() && state.drainerEntityId() != this.shadowed.getId())) {
            return false;
        }

        return this.shadowed.distanceToSqr(this.target) <= SEARCH_RADIUS * SEARCH_RADIUS;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        if (this.target instanceof ServerPlayer serverPlayer) {
            this.releaseDrain(serverPlayer);
        }

        this.shadowed.setDraining(false);
        this.target = null;
        this.giveUpCooldown = 100 + this.shadowed.getRandom().nextInt(200);
    }

    @Override
    public void tick() {
        if (!(this.target instanceof ServerPlayer serverPlayer)) {
            return;
        }

        this.shadowed.getLookControl().setLookAt(this.target, TURN_SPEED, TURN_SPEED);

        if (this.shadowed.distanceToSqr(this.target) > DRAIN_RANGE_SQR || !this.shadowed.getSensing().hasLineOfSight(this.target)) {
            this.releaseDrain(serverPlayer);
            this.shadowed.getNavigation().moveTo(this.target, 1.0);
            return;
        }

        this.shadowed.getNavigation().stop();

        if (!this.isFacing(this.target)) {
            return;
        }

        SoulStolenState state = Services.SOUL_STOLEN.get(serverPlayer);
        int drainTicks = state.drainTicks() + 1;

        if (drainTicks >= SoulStolenState.DRAIN_DURATION_TICKS) {
            Services.SOUL_STOLEN.set(serverPlayer, state.withDrain(0, -1).withFaceless(true).withStolenBy(Optional.of(this.shadowed.getUUID())));
            this.shadowed.setPersistenceRequired();
            this.shadowed.setDraining(false);
            this.target = null;
        }
        else {
            Services.SOUL_STOLEN.set(serverPlayer, state.withDrain(drainTicks, this.shadowed.getId()));
            this.shadowed.setDraining(true);
        }
    }

    private void releaseDrain(ServerPlayer player) {
        SoulStolenState state = Services.SOUL_STOLEN.get(player);

        if (state.drainerEntityId() == this.shadowed.getId()) {
            Services.SOUL_STOLEN.set(player, state.withDrain(0, -1));
        }

        this.shadowed.setDraining(false);
    }

    private boolean isFacing(Player player) {
        double dx = player.getX() - this.shadowed.getX();
        double dz = player.getZ() - this.shadowed.getZ();
        double dy = player.getEyeY() - this.shadowed.getEyeY();
        float yaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        float pitch = (float) -(Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * (180.0 / Math.PI));

        return Math.abs(Mth.wrapDegrees(yaw - this.shadowed.getYHeadRot())) <= FACING_YAW_TOLERANCE
                && Math.abs(Mth.wrapDegrees(pitch - this.shadowed.getXRot())) <= FACING_PITCH_TOLERANCE;
    }

    private static boolean isFreshTarget(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer) || player.isSpectator()) {
            return false;
        }

        SoulStolenState state = Services.SOUL_STOLEN.get(serverPlayer);
        return !state.faceless() && !state.isDraining();
    }
}
