package com.nyfaria.undeadtakeover.fluid;

import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.FluidInit;
import com.nyfaria.undeadtakeover.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;

public abstract class BloodFluid extends FlowingFluid {

    @Override
    public Fluid getFlowing() {
        return FluidInit.FLOWING_BLOOD.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.BLOOD.get();
    }

    @Override
    public Item getBucket() {
        return ItemInit.BLOOD_BUCKET.get();
    }

    @Override
    protected boolean canConvertToSource(ServerLevel level) {
        return level.getGameRules().get(GameRules.WATER_SOURCE_CONVERSION);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    protected void entityInside(Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier) {
        effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
    }

    @Override
    public int getSlopeFindDistance(LevelReader level) {
        return 2;
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState) {
        return BlockInit.BLOOD.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid other) {
        return other == FluidInit.BLOOD.get() || other == FluidInit.FLOWING_BLOOD.get();
    }

    @Override
    public int getDropOff(LevelReader level) {
        return 2;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 20;
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid other, Direction direction) {
        return direction == Direction.DOWN && !this.isSame(other);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    public static class Flowing extends BloodFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static class Source extends BloodFluid {
        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}
