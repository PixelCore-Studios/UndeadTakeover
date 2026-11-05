package com.nyfaria.undeadtakeover.entity.ai.goals;

import com.nyfaria.undeadtakeover.init.TagInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SnuffLightsGoal extends Goal {
    private static final int SEARCH_RADIUS = 12;
    private static final int SEARCH_HEIGHT = 6;
    private static final int GIVE_UP_TICKS = 200;

    private final PathfinderMob mob;
    private final double speed;
    private BlockPos target;
    private int searchCooldown;
    private int ticks;

    public SnuffLightsGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.searchCooldown > 0) {
            this.searchCooldown--;
            return false;
        }
        this.searchCooldown = reducedTickDelay(40);
        if (!(this.mob.level() instanceof ServerLevel level) || !level.getGameRules().get(GameRules.MOB_GRIEFING)) {
            return false;
        }
        this.target = BlockPos.findClosestMatch(this.mob.blockPosition(), SEARCH_RADIUS, SEARCH_HEIGHT, this::isLight).orElse(null);
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.ticks < GIVE_UP_TICKS && this.isLight(this.target);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.ticks = 0;
        this.moveToTarget();
    }

    @Override
    public void stop() {
        if (this.ticks >= GIVE_UP_TICKS) {
            this.searchCooldown = reducedTickDelay(100);
        }
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }
        this.ticks++;
        Vec3 center = Vec3.atCenterOf(this.target);
        this.mob.getLookControl().setLookAt(center);
        if (this.mob.getBoundingBox().inflate(1.5).contains(center)) {
            this.mob.level().destroyBlock(this.target, true, this.mob);
            this.target = null;
        } else if (this.ticks % 20 == 0) {
            this.moveToTarget();
        }
    }

    private void moveToTarget() {
        this.mob.getNavigation().moveTo(this.target.getX() + 0.5, this.target.getY(), this.target.getZ() + 0.5, this.speed);
    }

    private boolean isLight(BlockPos pos) {
        return this.mob.level().getBlockState(pos).is(TagInit.LANKY_SNUFFABLE);
    }
}
