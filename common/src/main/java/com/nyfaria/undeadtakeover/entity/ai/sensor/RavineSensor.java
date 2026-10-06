package com.nyfaria.undeadtakeover.entity.ai.sensor;

import com.nyfaria.undeadtakeover.entity.Gravebeak;
import com.nyfaria.undeadtakeover.init.MemoryModuleTypeInit;
import com.nyfaria.undeadtakeover.init.SensorTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

public class RavineSensor extends ExtendedSensor<Gravebeak> {
    private static final int SEARCH_RADIUS = 10;
    private static final int MIN_DROP = 8;

    private final List<MemoryModuleType<?>> memories = List.of(MemoryModuleTypeInit.RAVINE_POS.get());

    public RavineSensor() {
        this.scanRate(10);
        this.onlyScanIf(entity -> entity.isVehicle() && !entity.isDiving());
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return SensorTypeInit.RAVINE.get();
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return this.memories;
    }

    @Override
    protected void doTick(ServerLevel level, Gravebeak entity) {
        BrainUtil.setOrClearMemory(entity, MemoryModuleTypeInit.RAVINE_POS.get(), findNearestDrop(level, entity.blockPosition()));
    }

    private static BlockPos findNearestDrop(Level level, BlockPos origin) {
        BlockPos.MutableBlockPos column = new BlockPos.MutableBlockPos();
        BlockPos nearest = null;
        int nearestDist = Integer.MAX_VALUE;

        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                int dist = dx * dx + dz * dz;

                if (dist > SEARCH_RADIUS * SEARCH_RADIUS || dist >= nearestDist) {
                    continue;
                }

                column.set(origin.getX() + dx, origin.getY(), origin.getZ() + dz);

                if (isDrop(level, column) && hasStandableEdge(level, column)) {
                    nearest = column.immutable();
                    nearestDist = dist;
                }
            }
        }

        return nearest;
    }

    private static boolean isDrop(Level level, BlockPos top) {
        for (int y = 2; y >= -MIN_DROP; y--) {
            if (!isOpen(level, top.above(y))) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasStandableEdge(Level level, BlockPos top) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos edge = top.relative(direction);

            if (isOpen(level, edge) && isOpen(level, edge.above()) && !isOpen(level, edge.below())) {
                return true;
            }
        }

        return false;
    }

    private static boolean isOpen(Level level, BlockPos pos) {
        return level.getBlockState(pos).getCollisionShape(level, pos).isEmpty() && level.getFluidState(pos).isEmpty();
    }
}
