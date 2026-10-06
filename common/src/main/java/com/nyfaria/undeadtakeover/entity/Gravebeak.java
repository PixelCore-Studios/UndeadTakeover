package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import com.nyfaria.undeadtakeover.entity.ai.behavior.GrabTarget;
import com.nyfaria.undeadtakeover.entity.ai.behavior.LeapIntoRavine;
import com.nyfaria.undeadtakeover.entity.ai.behavior.SetWalkTargetToRavine;
import com.nyfaria.undeadtakeover.entity.ai.sensor.RavineSensor;
import com.nyfaria.undeadtakeover.init.ActivityInit;
import com.nyfaria.undeadtakeover.init.MemoryModuleTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.ActivityBuilder;
import net.tslat.smartbrainlib.api.core.behaviour.base.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.base.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtil;
import net.tslat.smartbrainlib.util.SensoryUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Gravebeak extends Monster implements GeoEntity, SmartBrainOwner<Gravebeak> {
    public static final String CONTROLLER = "main";
    public static final String GRAB_ANIM = "grab";

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.gravebeak.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.gravebeak.walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.gravebeak.run");
    private static final RawAnimation GRAB = RawAnimation.begin().thenPlay("animation.gravebeak.grab");
    private static final RawAnimation HOLD = RawAnimation.begin().thenLoop("animation.gravebeak.hold");
    private static final RawAnimation HOLD_WALK = RawAnimation.begin().thenLoop("animation.gravebeak.hold_walk");
    private static final RawAnimation HOLD_RUN = RawAnimation.begin().thenLoop("animation.gravebeak.hold_run");

    private static final double HOLD_HEIGHT = 0.95;
    private static final double HOLD_FORWARD = 0.85;
    private static final float RUN_SPEED = 1.5F;
    private static final float BREAK_FREE_CHANCE_PER_LOST_HEALTH = 2.0F;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int grabCooldown;
    private boolean struggleHeld;
    private boolean diving;
    private int diveTicks;

    public Gravebeak(EntityType<? extends Gravebeak> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(Gravebeak owner) {
        return List.of(
                new NearbyPlayersSensor<Gravebeak>().setPredicate((entity, player) -> !player.isSpectator() && !isHeldByGravebeak(player)),
                new HurtBySensor<>(),
                new RavineSensor());
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(Gravebeak owner) {
        return List.of(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>());
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(Gravebeak owner) {
        return List.of(
                new FirstApplicableBehaviour<Gravebeak>(
                        new TargetOrRetaliate<>().useMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>().speedModifier(1.0F),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(Gravebeak owner) {
        return List.of(
                new InvalidateAttackTarget<Gravebeak>().invalidateIf((entity, target) ->
                        (target instanceof Player player && player.getAbilities().invulnerable)
                                || !SensoryUtil.isInFollowRange(entity, target)
                                || !entity.canAttack(target)
                                || isHeldByGravebeak(target)),
                new SetWalkTargetToAttackTarget<>().speedModifier(RUN_SPEED),
                new GrabTarget());
    }

    @Override
    public Activity[] getActivityActivationPriority() {
        return new Activity[] {ActivityInit.CARRY.get(), Activity.FIGHT, Activity.IDLE};
    }

    @Override
    public ActivityBuilder<? extends Gravebeak> getActivityGroupFor(Activity activity) {
        if (activity != ActivityInit.CARRY.get()) {
            return SmartBrainOwner.super.getActivityGroupFor(activity);
        }

        return ActivityBuilder.<Gravebeak>create(activity)
                .addMemoryRequirements(MemoryModuleTypeInit.CARRYING.get())
                .behaviours(
                        new LeapIntoRavine(),
                        new SetWalkTargetToRavine(RUN_SPEED).cooldownFor(20),
                        new SetRandomWalkTarget<Gravebeak>()
                                .speedModifier(0.8F)
                                .startCondition(entity -> !entity.isDiving() && !BrainUtil.hasMemory(entity, MemoryModuleTypeInit.RAVINE_POS.get())));
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);

        if (this.grabCooldown > 0) {
            this.grabCooldown--;
        }

        if (this.diving && ++this.diveTicks > 5 && (this.onGround() || this.isInWater())) {
            this.ejectPassengers();
        }

        this.setAggressive(this.isVehicle()
                ? BrainUtil.hasMemory(this, MemoryModuleTypeInit.RAVINE_POS.get())
                : BrainUtil.hasMemory(this, MemoryModuleType.ATTACK_TARGET));
    }

    private static boolean isHeldByGravebeak(Entity entity) {
        return entity.getVehicle() instanceof Gravebeak;
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    public boolean canGrab() {
        return this.grabCooldown <= 0 && !this.isVehicle();
    }

    public boolean isDiving() {
        return this.diving;
    }

    public void grab(LivingEntity target) {
        target.startRiding(this, true, true);
    }

    public boolean tryBreakFree(boolean struggling) {
        boolean pressed = struggling && !this.struggleHeld;
        this.struggleHeld = struggling;

        return pressed && this.random.nextFloat() < this.getBreakFreeChance();
    }

    public float getBreakFreeChance() {
        float lostHealth = 1.0F - this.getHealth() / this.getMaxHealth();

        return Mth.clamp(lostHealth * BREAK_FREE_CHANCE_PER_LOST_HEALTH, 0.0F, 1.0F);
    }

    public void missGrab() {
        this.grabCooldown = 20;
    }

    public void leapInto(BlockPos ravine) {
        double dx = ravine.getX() + 0.5 - this.getX();
        double dz = ravine.getZ() + 0.5 - this.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);

        if (length > 0) {
            dx /= length;
            dz /= length;
        }

        this.getNavigation().stop();
        this.setYRot((float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F);
        this.yBodyRot = this.getYRot();
        this.setDeltaMovement(dx * 0.35, 0.35, dz * 0.35);
        this.needsSync = true;
        this.diving = true;
        this.diveTicks = 0;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty() && passenger instanceof Player;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);

        if (!this.level().isClientSide()) {
            BrainUtil.setMemory(this, MemoryModuleTypeInit.CARRYING.get(), Unit.INSTANCE);
            this.struggleHeld = true;
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);

        if (!this.level().isClientSide()) {
            BrainUtil.clearMemory(this, MemoryModuleTypeInit.CARRYING.get());
            BrainUtil.clearMemory(this, MemoryModuleTypeInit.RAVINE_POS.get());
            BrainUtil.clearMemory(this, MemoryModuleType.WALK_TARGET);
            this.grabCooldown = 100 + this.random.nextInt(60);
            this.diving = false;
        }
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0, HOLD_HEIGHT * scale, HOLD_FORWARD * scale).yRot(-this.yBodyRot * Mth.DEG_TO_RAD);
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
        boolean hurt = super.causeFallDamage(fallDistance, damageModifier, damageSource);

        for (Entity passenger : this.getPassengers()) {
            passenger.causeFallDamage(fallDistance, damageModifier, damageSource);
        }

        return hurt;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(CONTROLLER, 5, this::handleAnimation)
                .triggerableAnim(GRAB_ANIM, GRAB)
                .receiveTriggeredAnimations());
    }

    private PlayState handleAnimation(AnimationTest<Gravebeak> test) {
        AnimationController<Gravebeak> controller = test.controller();

        if (!this.isVehicle() && test.isCurrentAnimation(GRAB)
                && (controller.getCurrentAnimationPoint() == null || controller.isPlayingTriggeredAnimation())) {
            return PlayState.CONTINUE;
        }

        boolean running = this.isAggressive();

        if (this.isVehicle()) {
            if (test.isMoving()) {
                return test.setAndContinue(running ? HOLD_RUN : HOLD_WALK);
            }

            return test.setAndContinue(HOLD);
        }

        if (test.isMoving()) {
            return test.setAndContinue(running ? RUN : WALK);
        }

        return test.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
