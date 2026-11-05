package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.nyfaria.awcapi.ClimberHelper;
import com.nyfaria.awcapi.entity.ClimberComponent;
import com.nyfaria.awcapi.entity.IAdvancedClimber;
import com.nyfaria.awcapi.entity.movement.AdvancedClimberPathNavigator;
import com.nyfaria.undeadtakeover.entity.ai.goal.WigglerLeapAtPlayerGoal;
import com.nyfaria.undeadtakeover.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Wiggler extends Zombie implements GeoEntity, IAdvancedClimber {
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.wiggler.walk");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.wiggler.idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ClimberComponent climberComponent = new ClimberComponent(this);

    public Wiggler(EntityType<? extends Wiggler> type, Level level) {
        super(type, level);
        ClimberHelper.initClimber(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new WigglerLeapAtPlayerGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AdvancedClimberPathNavigator<>(this, level, true, true, false);
    }

    @Override
    public ClimberComponent getClimberComponent() {
        return this.climberComponent;
    }

    @Override
    public float getMovementSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public float getBlockSlipperiness(BlockPos pos) {
        return this.level().getBlockState(pos).getBlock().getFriction();
    }

    @Override
    public Mob asMob() {
        return this;
    }

    @Override
    public void setLerpYRot(Float yRot) {
    }

    @Override
    public void setLerpXRot(Float xRot) {
    }

    @Override
    public void setLerpYHeadRot(Float yHeadRot) {
    }

    @Override
    public void setLerpHeadSteps(int steps) {
    }

    @Override
    public void tick() {
        super.tick();
        ClimberHelper.tickClimber(this);
        if (this.level().isClientSide()) {
            this.spawnBloodSmear();
        }
    }

    private void spawnBloodSmear() {
        double dx = this.getX() - this.xo;
        double dy = this.getY() - this.yo;
        double dz = this.getZ() - this.zo;
        if (dx * dx + dy * dy + dz * dz < 4.0E-4 || this.isInWater() || this.random.nextInt(3) != 0) {
            return;
        }
        Direction side = this.climberComponent.getGroundSide();
        AABB box = this.getBoundingBox();
        double x = Mth.lerp(this.random.nextDouble(), box.minX + 0.1, box.maxX - 0.1);
        double y = Mth.lerp(this.random.nextDouble(), box.minY + 0.1, box.maxY - 0.1);
        double z = Mth.lerp(this.random.nextDouble(), box.minZ + 0.1, box.maxZ - 0.1);
        switch (side) {
            case DOWN -> y = box.minY;
            case UP -> y = box.maxY;
            case NORTH -> z = box.minZ;
            case SOUTH -> z = box.maxZ;
            case WEST -> x = box.minX;
            case EAST -> x = box.maxX;
        }
        BlockPos surface = BlockPos.containing(x + side.getStepX() * 0.1, y + side.getStepY() * 0.1, z + side.getStepZ() * 0.1);
        if (!this.level().getBlockState(surface).isFaceSturdy(this.level(), surface, side.getOpposite())) {
            return;
        }
        int step = side.getAxisDirection().getStep();
        double face = surface.get(side.getAxis()) + (step > 0 ? 0.0 : 1.0) - step * (0.015 + this.random.nextDouble() * 0.01);
        switch (side.getAxis()) {
            case X -> x = face;
            case Y -> y = face;
            case Z -> z = face;
        }
        this.level().addParticle(ParticleInit.BLOOD_SMEAR.get(), x, y, z, -side.getStepX(), -side.getStepY(), -side.getStepZ());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        ClimberHelper.livingTickClimber(this);
    }

    @Override
    public void travel(Vec3 relative) {
        if (ClimberHelper.handleTravel(this, relative)) {
            return;
        }

        super.travel(relative);
        ClimberHelper.postTravel(this, relative);
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        ClimberHelper.handleMove(this, type, pos, true);
        super.move(type, pos);
        ClimberHelper.handleMove(this, type, pos, false);
    }

    @Override
    public void jumpFromGround() {
        if (!ClimberHelper.handleJump(this)) {
            super.jumpFromGround();
        }
    }

    @Override
    public BlockPos getOnPos() {
        return ClimberHelper.getAdjustedOnPosition(this, super.getOnPos());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", test -> test.setAndContinue(test.isMoving() ? WALK : IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
