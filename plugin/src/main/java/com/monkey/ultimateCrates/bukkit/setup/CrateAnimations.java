package com.monkey.ultimateCrates.bukkit.setup;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.animation.AnimationManager;
import com.monkey.ultimateCrates.bukkit.crates.animation.list.*;

public class CrateAnimations {

    public static void registerAll(AnimationManager manager) {
        manager.registerAnimation(new SparkleAnimation());
        manager.registerAnimation(new CircleAnimation());
        manager.registerAnimation(new SpiralAnimation());
        manager.registerAnimation(new FireworksAnimation());
        manager.registerAnimation(new RisingSmokeAnimation());
        manager.registerAnimation(new HeartBeatAnimation());
        manager.registerAnimation(new RainAnimation());
        manager.registerAnimation(new FlameRingAnimation());
        manager.registerAnimation(new PortalSwirlAnimation());
        manager.registerAnimation(new BubbleUpAnimation());
        manager.registerAnimation(new TotemAnimation(UltimateCrates.getInstance()));
        manager.registerAnimation(new BoltAnimation(UltimateCrates.getInstance()));
    }
}
