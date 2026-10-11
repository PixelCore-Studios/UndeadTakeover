package com.nyfaria.undeadtakeover.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;

public record MausoleumInstance(BoundingBox box, List<BlockPos> chestPositions) {
    public int upperHalfMinY() {
        return this.box.minY() + (this.box.maxY() - this.box.minY()) / 2;
    }
}
