package com.monkey.ultimateCrates.bukkit.crates.animation.list;

import com.monkey.ultimateCrates.bukkit.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class CircleAnimation implements CrateAnimation {

    private static final int PARTICLES = 30;
    private static final double RADIUS = 1.0;

    @Override
    public void play(Player player, Location location) {
        Location base = location.clone().add(0, 1, 0);

        for (int i = 0; i < PARTICLES; i++) {
            double angle = 2 * Math.PI * i / PARTICLES;
            double x = RADIUS * Math.cos(angle);
            double z = RADIUS * Math.sin(angle);
            Location particleLoc = base.clone().add(x, 0, z);
            location.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public String getName() {
        return "circle";
    }
}
