package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SparkleAnimation implements CrateAnimation {

    @Override
    public void play(Player player, Location location) {
        location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 30, 0.5, 1, 0.5, 0.1);
    }

    @Override
    public String getName() {
        return "sparkle";
    }
}
