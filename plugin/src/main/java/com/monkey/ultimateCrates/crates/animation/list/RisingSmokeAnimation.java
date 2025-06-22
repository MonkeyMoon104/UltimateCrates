package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class RisingSmokeAnimation implements CrateAnimation {

    @Override
    public void play(Player player, Location location) {
        Location base = location.clone();

        for (int i = 0; i < 10; i++) {
            Location particleLoc = base.clone().add(0, i * 0.2, 0);
            location.getWorld().spawnParticle(Particle.SMOKE, particleLoc, 2, 0.1, 0.1, 0.1, 0.01);
        }
    }

    @Override
    public String getName() {
        return "rising_smoke";
    }
}
