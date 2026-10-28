package com.nyfaria.undeadtakeover.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class Shingled extends Zombie implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("shingled.idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Shingled(EntityType<? extends Shingled> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", test -> test.setAndContinue(IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
