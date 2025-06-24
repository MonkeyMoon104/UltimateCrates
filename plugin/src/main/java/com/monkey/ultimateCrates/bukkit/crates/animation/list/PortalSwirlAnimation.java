package com.monkey.ultimateCrates.bukkit.crates.animation.list;

import com.monkey.ultimateCrates.bukkit.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class PortalSwirlAnimation implements CrateAnimation {

    private static final int PARTICLES = 25;
    private static final double RADIUS = 1.0;
    private static final double HEIGHT = 1.5;

    @Override
    public void play(Player player, Location location) {
        Location base = location.clone();

        for (int i = 0; i < PARTICLES; i++) {
            double t = (double) i / PARTICLES;
            double angle = 4 * Math.PI * t;
            double y = HEIGHT * t;
            double x = RADIUS * Math.cos(angle);
            double z = RADIUS * Math.sin(angle);
            Location particleLoc = base.clone().add(x, y, z);
            location.getWorld().spawnParticle(Particle.PORTAL, particleLoc, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public String getName() {
        return "portal_swirl";
    }
}
