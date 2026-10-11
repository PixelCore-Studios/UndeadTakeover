package com.nyfaria.undeadtakeover.worldgen;

import com.nyfaria.undeadtakeover.entity.GraveTerror;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.init.StructureInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class GraveTerrorMausoleumManager {
    private static final int CHUNK_SEARCH_RADIUS = 2;
    private static final int CHECK_INTERVAL_TICKS = 40;

    private static final Map<ResourceKey<Level>, Map<BlockPos, MausoleumInstance>> INSTANCES = new ConcurrentHashMap<>();

    private GraveTerrorMausoleumManager() {
    }

    public static void tickNearby(ServerPlayer player) {
        if (player.tickCount % CHECK_INTERVAL_TICKS != 0) {
            return;
        }

        ServerLevel level = player.level();
        Holder<Structure> structureHolder = level.registryAccess().lookupOrThrow(Registries.STRUCTURE).getOrThrow(StructureInit.GREAT_MAUSOLEUM);
        Structure structure = structureHolder.value();

        Map<BlockPos, MausoleumInstance> levelMap = INSTANCES.computeIfAbsent(level.dimension(), key -> new ConcurrentHashMap<>());
        BlockPos playerPos = player.blockPosition();
        ChunkPos center = new ChunkPos(playerPos.getX() >> 4, playerPos.getZ() >> 4);

        for (int dx = -CHUNK_SEARCH_RADIUS; dx <= CHUNK_SEARCH_RADIUS; dx++) {
            for (int dz = -CHUNK_SEARCH_RADIUS; dz <= CHUNK_SEARCH_RADIUS; dz++) {
                ChunkPos chunkPos = new ChunkPos(center.x() + dx, center.z() + dz);
                LevelChunk chunk = level.getChunkSource().getChunkNow(chunkPos.x(), chunkPos.z());

                if (chunk == null) {
                    continue;
                }

                StructureStart start = chunk.getStartForStructure(structure);

                if (start == null || !start.isValid() || !start.getChunkPos().equals(chunkPos)) {
                    continue;
                }

                BlockPos key = new BlockPos(start.getBoundingBox().minX(), start.getBoundingBox().minY(), start.getBoundingBox().minZ());
                MausoleumInstance instance = levelMap.computeIfAbsent(key, k -> buildInstance(level, start));

                ensureBoss(level, instance);
            }
        }
    }

    private static MausoleumInstance buildInstance(ServerLevel level, StructureStart start) {
        List<BlockPos> chests = new ArrayList<>();

        for (StructurePiece piece : start.getPieces()) {
            if (!(piece instanceof PoolElementStructurePiece poolPiece) || !(poolPiece.getElement() instanceof SinglePoolElement single)) {
                continue;
            }

            Optional<StructureTemplate> template = level.getStructureManager().get(single.getTemplateLocation());

            if (template.isEmpty()) {
                continue;
            }

            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(poolPiece.getRotation());

            for (StructureTemplate.StructureBlockInfo info : template.get().filterBlocks(poolPiece.getPosition(), settings, Blocks.CHEST)) {
                chests.add(info.pos());
            }
        }

        return new MausoleumInstance(start.getBoundingBox(), chests);
    }

    private static void ensureBoss(ServerLevel level, MausoleumInstance instance) {
        AABB searchBox = AABB.of(instance.box());
        boolean alive = !level.getEntitiesOfClass(GraveTerror.class, searchBox).isEmpty();

        if (alive) {
            return;
        }

        GraveTerror boss = EntityInit.GRAVE_TERROR.get().create(level, EntitySpawnReason.STRUCTURE);

        if (boss == null) {
            return;
        }

        BoundingBox box = instance.box();
        double x = (box.minX() + box.maxX()) / 2.0 + 0.5;
        double y = instance.upperHalfMinY() + 1;
        double z = (box.minZ() + box.maxZ()) / 2.0 + 0.5;

        boss.snapTo(x, y, z, 0.0F, 0.0F);
        boss.setMausoleum(instance);
        level.addFreshEntity(boss);
    }

    public static Optional<MausoleumInstance> findContaining(ServerLevel level, BlockPos pos) {
        Map<BlockPos, MausoleumInstance> levelMap = INSTANCES.get(level.dimension());

        if (levelMap == null) {
            return Optional.empty();
        }

        return levelMap.values().stream().filter(instance -> instance.box().isInside(pos)).findFirst();
    }
}
