package com.nyfaria.undeadtakeover.block;

import com.mojang.serialization.MapCodec;
import com.nyfaria.undeadtakeover.block.entity.RevenantAnchorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class RevenantPortalBlock extends Block implements Portal {
    public static final MapCodec<RevenantPortalBlock> CODEC = simpleCodec(RevenantPortalBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Block.column(4.0, 16.0, 0.0, 16.0));
    private static final DustParticleOptions PARTICLE = new DustParticleOptions(0xB01010, 1.0F);

    public RevenantPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public MapCodec<RevenantPortalBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(AXIS));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return entity instanceof Player player
                ? Math.max(0, level.getGameRules().get(player.getAbilities().invulnerable ? GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY))
                : 0;
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
        RevenantAnchorBlockEntity anchor = RevenantAnchorBlockEntity.findNear(currentLevel, portalEntryPos);
        return anchor != null ? anchor.createTransition(currentLevel, entity) : null;
    }

    @Override
    public Portal.Transition getLocalTransition() {
        return Portal.Transition.CONFUSION;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 4; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            double offset = (random.nextInt(2) * 2 - 1) * 0.25;

            if (state.getValue(AXIS) == Direction.Axis.X) {
                z = pos.getZ() + 0.5 + offset;
            } else {
                x = pos.getX() + 0.5 + offset;
            }

            level.addParticle(PARTICLE, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> state.setValue(AXIS, state.getValue(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
            default -> state;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
}
