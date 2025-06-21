package com.monkey.ultimateCrates.crates.animation;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnimationManager {

    private final UltimateCrates plugin;
    private final Map<String, CrateAnimation> animations = new HashMap<>();

    public AnimationManager(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void registerAnimation(CrateAnimation animation) {
        animations.put(animation.getName().toLowerCase(), animation);
        plugin.getLogger().info("Registrata animazione: " + animation.getName());
    }

    public void clear() {
        animations.clear();
        plugin.getLogger().info("Animations rimosse");
    }

    public Optional<CrateAnimation> getAnimation(String name) {
        return Optional.ofNullable(animations.get(name.toLowerCase()));
    }
}
