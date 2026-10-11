package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.nyfaria.undeadtakeover.entity.ai.behavior.SetRandomAirWalkTarget;
import com.nyfaria.undeadtakeover.entity.ai.behavior.SummonUndeadBehaviour;
import com.nyfaria.undeadtakeover.init.ActivityInit;
import com.nyfaria.undeadtakeover.init.LootTableInit;
import com.nyfaria.undeadtakeover.init.MemoryModuleTypeInit;
import com.nyfaria.undeadtakeover.worldgen.MausoleumInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.BossEvent;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.ActivityBuilder;
import net.tslat.smartbrainlib.api.core.behaviour.base.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.base.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

public class GraveTerror extends PathfinderMob implements GeoEntity, SmartBrainOwner<GraveTerror> {
    public static final int SHIELD_KILLS_REQUIRED = 20;
    private static final float GROUND_SPEED = 1.0F;

    private static final EntityDataAccessor<Byte> DATA_PHASE = SynchedEntityData.defineId(GraveTerror.class, EntityDataSerializers.BYTE);
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.grave_terror.fly");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.grave_terror.walk");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.grave_terror.idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ServerBossEvent bossEvent;

    private int shieldKillsRemaining = SHIELD_KILLS_REQUIRED;
    private MausoleumInstance mausoleum;

    public GraveTerror(EntityType<? extends GraveTerror> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
        this.xpReward = 50;
        this.bossEvent = new ServerBossEvent(this.getUUID(), this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FLYING_SPEED, 0.4)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_PHASE, (byte) GraveTerrorPhase.AIR.ordinal());
    }

    public GraveTerrorPhase getPhase() {
        return GraveTerrorPhase.values()[this.entityData.get(DATA_PHASE)];
    }

    private void setPhase(GraveTerrorPhase phase) {
        this.entityData.set(DATA_PHASE, (byte) phase.ordinal());
    }

    public boolean isAirPhase() {
        return this.getPhase() == GraveTerrorPhase.AIR;
    }

    public void setMausoleum(MausoleumInstance mausoleum) {
        this.mausoleum = mausoleum;
    }

    public MausoleumInstance getMausoleum() {
        return this.mausoleum;
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(GraveTerror owner) {
        return List.of(
                new NearbyPlayersSensor<GraveTerror>().setPredicate((entity, player) -> !player.isSpectator()));
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(GraveTerror owner) {
        return List.of(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>());
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(GraveTerror owner) {
        return List.of(
                new FirstApplicableBehaviour<GraveTerror>(
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>().speedModifier(GROUND_SPEED),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public Activity[] getActivityActivationPriority() {
        return new Activity[] {ActivityInit.GRAVE_TERROR_AIR.get(), Activity.IDLE};
    }

    @Override
    public ActivityBuilder<? extends GraveTerror> getActivityGroupFor(Activity activity) {
        if (activity != ActivityInit.GRAVE_TERROR_AIR.get()) {
            return SmartBrainOwner.super.getActivityGroupFor(activity);
        }

        return ActivityBuilder.<GraveTerror>create(activity)
                .addMemoryRequirements(MemoryModuleTypeInit.GRAVE_TERROR_AIR_PHASE.get())
                .behaviours(
                        new SetRandomAirWalkTarget(1.0F).cooldownFor(40),
                        new SummonUndeadBehaviour().cooldownFor(1200));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isAirPhase()) {
            return false;
        }

        return super.hurtServer(level, source, amount);
    }

    public void onNearbyUndeadKilled() {
        if (!this.isAirPhase() || this.shieldKillsRemaining <= 0) {
            return;
        }

        this.shieldKillsRemaining--;
        this.bossEvent.setProgress(this.shieldKillsRemaining / (float) SHIELD_KILLS_REQUIRED);

        if (this.shieldKillsRemaining <= 0) {
            this.breakShield();
        }
    }

    private void breakShield() {
        this.setPhase(GraveTerrorPhase.GROUND);
        this.setNoGravity(false);
        this.moveControl = new MoveControl(this);
        this.navigation = new GroundPathNavigation(this, this.level());
        this.bossEvent.setColor(BossEvent.BossBarColor.RED);
        this.bossEvent.setProgress(1.0F);
    }

    @Override
    public void checkDespawn() {
        this.noActionTime = 0;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);

        if (this.isAirPhase()) {
            BrainUtil.setMemory(this, MemoryModuleTypeInit.GRAVE_TERROR_AIR_PHASE.get(), Unit.INSTANCE);
        }
        else {
            BrainUtil.clearMemory(this, MemoryModuleTypeInit.GRAVE_TERROR_AIR_PHASE.get());
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        this.bossEvent.removeAllPlayers();

        if (this.level() instanceof ServerLevel serverLevel && this.mausoleum != null) {
            for (BlockPos pos : this.mausoleum.chestPositions()) {
                RandomizableContainer.setBlockEntityLootTable(serverLevel, serverLevel.getRandom(), pos, LootTableInit.GREAT_MAUSOLEUM_CHEST);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", test -> {
            if (this.isAirPhase()) {
                return test.setAndContinue(FLY);
            }

            return test.setAndContinue(test.isMoving() ? WALK : IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
