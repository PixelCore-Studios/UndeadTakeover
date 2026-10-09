package com.nyfaria.undeadtakeover.entity.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HoverNearGroundGoal extends Goal {
    private static final int HORIZONTAL_RANGE = 8;
    private static final int SEARCH_UP = 3;
    private static final int SEARCH_DOWN = 6;

    private final PathfinderMob mob;
    private final double speed;
    private final double hoverHeight;

    public HoverNearGroundGoal(PathfinderMob mob, double speed, double hoverHeight) {
        this.mob = mob;
        this.speed = speed;
        this.hoverHeight = hoverHeight;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.getNavigation().isDone() && this.mob.getRandom().nextInt(20) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getNavigation().isInProgress();
    }

    @Override
    public void start() {
        Vec3 pos = this.findPos();

        if (pos != null) {
            this.mob.getNavigation().moveTo(pos.x, pos.y, pos.z, this.speed);
        }
    }

    private Vec3 findPos() {
        RandomSource random = this.mob.getRandom();
        int baseY = Mth.floor(this.mob.getY());

        for (int attempt = 0; attempt < 10; attempt++) {
            int x = Mth.floor(this.mob.getX()) + random.nextInt(HORIZONTAL_RANGE * 2 + 1) - HORIZONTAL_RANGE;
            int z = Mth.floor(this.mob.getZ()) + random.nextInt(HORIZONTAL_RANGE * 2 + 1) - HORIZONTAL_RANGE;

            for (int y = baseY + SEARCH_UP; y >= baseY - SEARCH_DOWN; y--) {
                BlockPos ground = new BlockPos(x, y, z);

                if (isHoverSpot(this.mob.level(), ground)) {
                    return new Vec3(x + 0.5, y + 1 + this.hoverHeight, z + 0.5);
                }
            }
        }

        return null;
    }

    private static boolean isHoverSpot(Level level, BlockPos ground) {
        if (!level.getBlockState(ground).isFaceSturdy(level, ground, Direction.UP)) {
            return false;
        }

        for (int i = 1; i <= 4; i++) {
            BlockPos above = ground.above(i);

            if (!level.getBlockState(above).getCollisionShape(level, above).isEmpty() || !level.getFluidState(above).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
