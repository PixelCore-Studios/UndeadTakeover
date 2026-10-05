package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.nyfaria.undeadtakeover.entity.ai.goals.BiteGoal;
import com.nyfaria.undeadtakeover.entity.ai.goals.SporadicFlightGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BloodSipper extends Monster implements GeoEntity {
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.blood_sipper.fly");
    private static final int BITE_WEAKNESS_TICKS = 100;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int biteCooldown;

    public BloodSipper(EntityType<? extends BloodSipper> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BiteGoal(this));
        this.goalSelector.addGoal(2, new SporadicFlightGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
        if (this.biteCooldown > 0) {
            this.biteCooldown--;
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean bitten = super.doHurtTarget(level, target);
        if (bitten && target instanceof LivingEntity living) {
            this.applyBiteWeakness(living);
            this.biteCooldown = 40 + this.random.nextInt(40);
        }
        return bitten;
    }

    private void applyBiteWeakness(LivingEntity target) {
        MobEffectInstance current = target.getEffect(MobEffects.WEAKNESS);
        if (current != null && current.isInfiniteDuration()) {
            return;
        }
        int duration = BITE_WEAKNESS_TICKS + (current != null ? current.getDuration() : 0);
        int amplifier = current != null ? current.getAmplifier() : 0;
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, amplifier), this);
    }

    public boolean canBite() {
        return this.biteCooldown <= 0;
    }

    @Override
    protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("fly", test -> test.setAndContinue(FLY)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
