package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class SpiralAnimation implements CrateAnimation {

    private static final int TURNS = 3;
    private static final int PARTICLES = 40;
    private static final double HEIGHT = 2.0;
    private static final double RADIUS = 0.7;

    @Override
    public void play(Player player, Location location) {
        Location base = location.clone();

        for (int i = 0; i < PARTICLES; i++) {
            double t = (double) i / PARTICLES;
            double angle = TURNS * 2 * Math.PI * t;
            double y = HEIGHT * t;
            double x = RADIUS * Math.cos(angle);
            double z = RADIUS * Math.sin(angle);
            Location particleLoc = base.clone().add(x, y, z);
            location.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public String getName() {
        return "spiral";
    }
}
