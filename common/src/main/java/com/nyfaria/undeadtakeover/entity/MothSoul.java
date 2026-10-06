package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.config.UndeadTakeoverConfig;
import com.nyfaria.undeadtakeover.entity.ai.goals.SiphonPlayerGoal;
import com.nyfaria.undeadtakeover.entity.ai.goals.SporadicFlightGoal;
import com.nyfaria.undeadtakeover.entity.data.*;
import com.nyfaria.undeadtakeover.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
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
import net.minecraft.world.level.storage.TagValueOutput;

public class MothSoul extends PathfinderMob implements GeoEntity {
    private static final Identifier SIPHON_MODIFIER_ID = Constants.modLoc("moth_soul_siphon");
    private static final double MAX_HEALTH_FLOOR = 1.0;

    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.moth_soul.fly");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.moth_soul.idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean perchedForRender;

    public MothSoul(EntityType<? extends MothSoul> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SiphonPlayerGoal(this));
        this.goalSelector.addGoal(2, new SporadicFlightGoal(this));
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

    /**
     * Serializes this moth onto the given player's shoulder, vanilla-parrot style: on success the real
     * entity is discarded entirely, with only data left on the player. Returns false if both shoulders
     * are already occupied, the player is airborne/swimming/etc, or otherwise ineligible.
     */
    public boolean perchOnShoulder(ServerPlayer player) {
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), Constants.LOG)) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, this.registryAccess());
            this.saveWithoutId(output);
            output.putString("id", this.getEncodeId());
            var tag = output.buildResult();

            if (!player.setEntityOnShoulder(tag)) {
                return false;
            }

            boolean left = player.getShoulderEntityLeft() == tag;
            MothAttachState current = Services.MOTH_SHOULDER.get(player);
            Services.MOTH_SHOULDER.set(player, (left ? current.withLeft(true) : current.withRight(true)).withSitTicks(0));
            this.discard();
            return true;
        }
    }

    public static void siphonMaxHealth(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        AttributeModifier existing = maxHealth.getModifier(SIPHON_MODIFIER_ID);
        double drainedSoFar = existing != null ? -existing.amount() : 0.0;
        double newDrained = Math.min(drainedSoFar + UndeadTakeoverConfig.siphonAmount(), maxHealth.getBaseValue() - MAX_HEALTH_FLOOR);

        if (newDrained <= 0.0) {
            return;
        }

        maxHealth.addOrReplacePermanentModifier(new AttributeModifier(SIPHON_MODIFIER_ID, -newDrained, AttributeModifier.Operation.ADD_VALUE));
    }

    public static void cureMaxHealth(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth != null) {
            maxHealth.removeModifier(SIPHON_MODIFIER_ID);
        }
    }

    public void setPerchedForRender(boolean perchedForRender) {
        this.perchedForRender = perchedForRender;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", test -> test.setAndContinue(this.perchedForRender ? IDLE : FLY)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
