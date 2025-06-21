package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Random;

public class RainAnimation implements CrateAnimation {

    private final Random random = new Random();

    @Override
    public void play(Player player, Location location) {
        Location base = location.clone().add(0, 2, 0);

        for (int i = 0; i < 20; i++) {
            double x = (random.nextDouble() - 0.5) * 2;
            double z = (random.nextDouble() - 0.5) * 2;
            Location particleLoc = base.clone().add(x, 0, z);
            location.getWorld().spawnParticle(Particle.FALLING_WATER, particleLoc, 1, 0, -0.1, 0, 0.05);
        }
    }

    @Override
    public String getName() {
        return "rain";
    }
}
