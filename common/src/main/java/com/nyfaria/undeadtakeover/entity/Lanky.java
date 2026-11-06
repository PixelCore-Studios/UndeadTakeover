package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.nyfaria.undeadtakeover.entity.ai.goals.SnuffLightsGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Lanky extends Monster implements GeoEntity {
    public static final String CONTROLLER = "main";
    public static final String SNUFF_ANIM = "snuff";

    private static final EntityDataAccessor<Boolean> DATA_WATCHED = SynchedEntityData.defineId(Lanky.class, EntityDataSerializers.BOOLEAN);
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.lanky.walk");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.lanky.idle");
    private static final RawAnimation STARE = RawAnimation.begin().thenLoop("animation.lanky.stare");
    private static final RawAnimation SNUFF = RawAnimation.begin().thenPlay("animation.lanky.snuff");
    private static final double WATCH_RANGE = 64.0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Lanky(EntityType<? extends Lanky> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_WATCHED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SnuffLightsGoal(this, 1.0));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            this.entityData.set(DATA_WATCHED, this.isBeingWatched());
        }
        super.tick();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isWatched();
    }

    public boolean isWatched() {
        return this.entityData.get(DATA_WATCHED);
    }

    private boolean isBeingWatched() {
        for (Player player : this.level().players()) {
            if (player.isSpectator() || !player.isAlive() || player.distanceToSqr(this) > WATCH_RANGE * WATCH_RANGE) {
                continue;
            }
            if (this.isLookingAtMe(player, 0.05, true, false, this.getEyeY(), this.getY(0.5), this.getY(0.15))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(CONTROLLER, 5, test -> {
            if (this.isWatched()) {
                return test.setAndContinue(STARE);
            }

            return test.setAndContinue(test.isMoving() ? WALK : IDLE);
        }).triggerableAnim(SNUFF_ANIM, SNUFF));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
