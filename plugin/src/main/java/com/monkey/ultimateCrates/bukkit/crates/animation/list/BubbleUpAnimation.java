package com.monkey.ultimateCrates.bukkit.crates.animation.list;

import com.monkey.ultimateCrates.bukkit.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BubbleUpAnimation implements CrateAnimation {

    @Override
    public void play(Player player, Location location) {
        Location base = location.clone();

        for (int i = 0; i < 10; i++) {
            Location particleLoc = base.clone().add(0, i * 0.3, 0);
            location.getWorld().spawnParticle(Particle.BUBBLE_COLUMN_UP, particleLoc, 2, 0.1, 0.1, 0.1, 0.02);
        }
    }

    @Override
    public String getName() {
        return "bubble_up";
    }
}
