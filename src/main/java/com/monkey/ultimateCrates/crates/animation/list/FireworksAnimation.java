package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Random;

public class FireworksAnimation implements CrateAnimation {

    private final Random random = new Random();

    @Override
    public void play(Player player, Location location) {
        Location base = location.clone().add(0, 1, 0);

        for (int i = 0; i < 50; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2;
            double offsetY = (random.nextDouble() - 0.5) * 2;
            double offsetZ = (random.nextDouble() - 0.5) * 2;
            Location particleLoc = base.clone().add(offsetX, offsetY, offsetZ);
            location.getWorld().spawnParticle(Particle.FIREWORK, particleLoc, 1, 0, 0, 0, 0);
        }
    }

    @Override
    public String getName() {
        return "fireworks";
    }
}
