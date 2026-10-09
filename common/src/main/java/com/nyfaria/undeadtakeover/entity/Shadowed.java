package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.entity.ai.goals.DrainSoulGoal;
import com.nyfaria.undeadtakeover.entity.ai.goals.HoverNearGroundGoal;
import com.nyfaria.undeadtakeover.entity.data.SoulStolenState;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class Shadowed extends PathfinderMob implements GeoEntity {
    private static final Identifier PARALYSIS_MODIFIER_ID = Constants.modLoc("soul_drain_paralysis");
    public static final double HOVER_HEIGHT = 0.6;
    private static final double HOVER_RESPONSE = 0.15;
    private static final double MAX_VERTICAL_SPEED = 0.2;
    private static final int GROUND_SCAN_DEPTH = 32;
    private static final EntityDataAccessor<Boolean> DATA_DRAINING = SynchedEntityData.defineId(Shadowed.class, EntityDataSerializers.BOOLEAN);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.shadowed.idle");
    private static final RawAnimation SOUL_SUCK = RawAnimation.begin().thenLoop("animation.shadowed.soul_suck");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Shadowed(EntityType<? extends Shadowed> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_DRAINING, false);
    }

    public boolean isDraining() {
        return this.entityData.get(DATA_DRAINING);
    }

    public void setDraining(boolean draining) {
        this.entityData.set(DATA_DRAINING, draining);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.FLYING_SPEED, 0.3)
                .add(Attributes.MOVEMENT_SPEED, 0.15);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new DrainSoulGoal(this));
        this.goalSelector.addGoal(2, new HoverNearGroundGoal(this, 0.8, HOVER_HEIGHT));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    public void travel(Vec3 input) {
        if (!this.level().isClientSide()) {
            double ground = this.findGroundTop();
            double verticalSpeed = ground == Double.NEGATIVE_INFINITY
                    ? -MAX_VERTICAL_SPEED
                    : Mth.clamp((ground + HOVER_HEIGHT - this.getY()) * HOVER_RESPONSE, -MAX_VERTICAL_SPEED, MAX_VERTICAL_SPEED);
            Vec3 motion = this.getDeltaMovement();

            this.setDeltaMovement(motion.x, verticalSpeed, motion.z);
            input = new Vec3(input.x, 0.0, input.z);
        }

        super.travel(input);
    }

    private double findGroundTop() {
        BlockPos.MutableBlockPos pos = this.blockPosition().mutable();

        for (int i = 0; i < GROUND_SCAN_DEPTH; i++) {
            FluidState fluid = this.level().getFluidState(pos);

            if (!fluid.isEmpty()) {
                return pos.getY() + fluid.getHeight(this.level(), pos);
            }

            VoxelShape shape = this.level().getBlockState(pos).getCollisionShape(this.level(), pos);

            if (!shape.isEmpty()) {
                return pos.getY() + shape.max(Direction.Axis.Y);
            }

            pos.move(Direction.DOWN);
        }

        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public void checkDespawn() {
        if (this.isPersistenceRequired()) {
            this.noActionTime = 0;
            return;
        }

        super.checkDespawn();
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);

        if (this.level() instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
                SoulStolenState state = Services.SOUL_STOLEN.get(player);

                if (state.stolenBy().equals(Optional.of(this.getUUID()))) {
                    Services.SOUL_STOLEN.set(player, SoulStolenState.NONE);
                }
            }
        }
    }

    public static void setParalyzed(Player player, boolean paralyzed) {
        setParalysisModifier(player.getAttribute(Attributes.MOVEMENT_SPEED), paralyzed);
        setParalysisModifier(player.getAttribute(Attributes.JUMP_STRENGTH), paralyzed);
    }

    private static void setParalysisModifier(AttributeInstance attribute, boolean present) {
        if (attribute == null) {
            return;
        }

        if (present && !attribute.hasModifier(PARALYSIS_MODIFIER_ID)) {
            attribute.addTransientModifier(new AttributeModifier(PARALYSIS_MODIFIER_ID, -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        else if (!present && attribute.hasModifier(PARALYSIS_MODIFIER_ID)) {
            attribute.removeModifier(PARALYSIS_MODIFIER_ID);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", test -> test.setAndContinue(this.isDraining() ? SOUL_SUCK : IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
