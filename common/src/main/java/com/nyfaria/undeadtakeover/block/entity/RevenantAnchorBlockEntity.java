package com.nyfaria.undeadtakeover.block.entity;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.block.RevenantAnchorBlock;
import com.nyfaria.undeadtakeover.block.RevenantPortalBlock;
import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.DimensionInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RevenantAnchorBlockEntity extends BlockEntity {
    public static final int KILLS_REQUIRED = 10;
    public static final int KILL_RADIUS = 16;
    private static final int SEARCH_RADIUS = 24;
    private static final int MAX_PORTAL_WIDTH = 10;
    private static final int MAX_PORTAL_HEIGHT = 21;
    private static final Identifier PORTAL_TEMPLATE = Constants.modLoc("graveyard/portal/revanent_portal");
    private static final BlockPos TEMPLATE_ANCHOR = new BlockPos(3, 0, 5);

    private int kills;
    private @Nullable ResourceKey<Level> linkedLevel;
    private @Nullable BlockPos linkedPos;

    public RevenantAnchorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.REVENANT_ANCHOR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void onUndeadKilled(ServerLevel level, BlockPos deathPos) {
        List<RevenantAnchorBlockEntity> anchors = new ArrayList<>();

        for (RevenantAnchorBlockEntity anchor : findAll(level, deathPos, KILL_RADIUS)) {
            if (!anchor.isOpen()) {
                anchors.add(anchor);
            }
        }

        anchors.forEach(anchor -> anchor.addKill(level));
    }

    public static @Nullable RevenantAnchorBlockEntity findNear(ServerLevel level, BlockPos pos) {
        RevenantAnchorBlockEntity closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (RevenantAnchorBlockEntity anchor : findAll(level, pos, SEARCH_RADIUS)) {
            double distance = anchor.getBlockPos().distSqr(pos);

            if (anchor.isOpen() && distance < closestDistance) {
                closest = anchor;
                closestDistance = distance;
            }
        }

        return closest;
    }

    private static List<RevenantAnchorBlockEntity> findAll(ServerLevel level, BlockPos center, int radius) {
        List<RevenantAnchorBlockEntity> anchors = new ArrayList<>();

        for (int chunkX = (center.getX() - radius) >> 4; chunkX <= (center.getX() + radius) >> 4; chunkX++) {
            for (int chunkZ = (center.getZ() - radius) >> 4; chunkZ <= (center.getZ() + radius) >> 4; chunkZ++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(chunkX, chunkZ);

                if (chunk == null) {
                    continue;
                }

                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (blockEntity instanceof RevenantAnchorBlockEntity anchor && anchor.getBlockPos().closerThan(center, radius)) {
                        anchors.add(anchor);
                    }
                }
            }
        }

        return anchors;
    }

    public boolean isOpen() {
        return this.linkedLevel != null && this.linkedPos != null;
    }

    private void addKill(ServerLevel level) {
        this.kills++;
        int charge = Math.min(4, this.kills * 4 / KILLS_REQUIRED);
        BlockState state = this.getBlockState();

        if (charge != state.getValue(RevenantAnchorBlock.CHARGE)) {
            level.setBlock(this.worldPosition, state.setValue(RevenantAnchorBlock.CHARGE, charge), Block.UPDATE_ALL);
            level.playSound(null, this.worldPosition, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        this.setChanged();

        if (this.kills >= KILLS_REQUIRED) {
            this.open(level);
        }
    }

    private void open(ServerLevel level) {
        ResourceKey<Level> targetKey = level.dimension() == DimensionInit.THE_REVENANT ? Level.OVERWORLD : DimensionInit.THE_REVENANT;
        ServerLevel target = level.getServer().getLevel(targetKey);

        if (target == null) {
            return;
        }

        this.lightPortal(level);
        this.connect(level, target, this.worldPosition);
        level.playSound(null, this.worldPosition, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void connect(ServerLevel level, ServerLevel target, BlockPos near) {
        BlockPos otherPos = buildPortal(target, near, this.getBlockState().getValue(RevenantAnchorBlock.FACING));

        if (otherPos == null || !(target.getBlockEntity(otherPos) instanceof RevenantAnchorBlockEntity other)) {
            return;
        }

        this.linkedLevel = target.dimension();
        this.linkedPos = otherPos;
        this.setChanged();

        other.kills = KILLS_REQUIRED;
        other.linkedLevel = level.dimension();
        other.linkedPos = this.worldPosition;
        other.lightPortal(target);
        other.setChanged();
    }

    private void lightPortal(ServerLevel level) {
        BlockState state = this.getBlockState().setValue(RevenantAnchorBlock.CHARGE, 4);
        level.setBlock(this.worldPosition, state, Block.UPDATE_ALL);

        Direction facing = state.getValue(RevenantAnchorBlock.FACING);
        Direction side = facing.getClockWise();
        BlockPos base = this.worldPosition.relative(facing).above();

        if (!isOpening(level, base)) {
            return;
        }

        int left = 0;
        while (left < MAX_PORTAL_WIDTH && isOpening(level, base.relative(side.getOpposite(), left + 1))) {
            left++;
        }

        int right = 0;
        while (right < MAX_PORTAL_WIDTH && isOpening(level, base.relative(side, right + 1))) {
            right++;
        }

        int height = 1;
        while (height < MAX_PORTAL_HEIGHT && isOpening(level, base.above(height))) {
            height++;
        }

        BlockState portal = BlockInit.REVENANT_PORTAL.get().defaultBlockState().setValue(RevenantPortalBlock.AXIS, side.getAxis());

        for (int offset = -left; offset <= right; offset++) {
            for (int y = 0; y < height; y++) {
                BlockPos pos = base.relative(side, offset).above(y);

                if (isOpening(level, pos)) {
                    level.setBlock(pos, portal, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                }
            }
        }
    }

    private static boolean isOpening(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.is(BlockInit.REVENANT_PORTAL.get());
    }

    private static @Nullable BlockPos buildPortal(ServerLevel level, BlockPos near, Direction facing) {
        Optional<StructureTemplate> template = level.getStructureManager().get(PORTAL_TEMPLATE);

        if (template.isEmpty()) {
            return null;
        }

        Rotation rotation = Rotation.NONE;
        for (Rotation candidate : Rotation.values()) {
            if (candidate.rotate(Direction.WEST) == facing) {
                rotation = candidate;
            }
        }

        level.getChunk(near.getX() >> 4, near.getZ() >> 4);
        int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, near.getX(), near.getZ());
        BlockPos anchorPos = new BlockPos(near.getX(), Mth.clamp(surface, level.getMinY() + 1, level.getMaxY() - 10), near.getZ());
        BlockPos origin = anchorPos.subtract(StructureTemplate.transform(TEMPLATE_ANCHOR, Mirror.NONE, rotation, BlockPos.ZERO));

        template.get().placeInWorld(level, origin, origin, new StructurePlaceSettings().setRotation(rotation), level.getRandom(), Block.UPDATE_ALL);
        return anchorPos;
    }

    public @Nullable TeleportTransition createTransition(ServerLevel level, Entity entity) {
        if (this.linkedLevel == null || this.linkedPos == null) {
            return null;
        }

        ServerLevel target = level.getServer().getLevel(this.linkedLevel);

        if (target == null) {
            return null;
        }

        if (!(target.getBlockEntity(this.linkedPos) instanceof RevenantAnchorBlockEntity)) {
            this.connect(level, target, this.linkedPos);
        }

        BlockState exitState = target.getBlockState(this.linkedPos);
        Direction facing = exitState.getOptionalValue(RevenantAnchorBlock.FACING).orElse(Direction.NORTH);

        return new TeleportTransition(
                target,
                Vec3.atBottomCenterOf(this.linkedPos.above()),
                Vec3.ZERO,
                facing.getOpposite().toYRot(),
                0.0F,
                Set.<Relative>of(),
                TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Kills", this.kills);

        if (this.linkedLevel != null && this.linkedPos != null) {
            output.store("LinkedLevel", Level.RESOURCE_KEY_CODEC, this.linkedLevel);
            output.store("LinkedPos", BlockPos.CODEC, this.linkedPos);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.kills = input.getIntOr("Kills", 0);
        this.linkedLevel = input.read("LinkedLevel", Level.RESOURCE_KEY_CODEC).orElse(null);
        this.linkedPos = input.read("LinkedPos", BlockPos.CODEC).orElse(null);
    }
}
