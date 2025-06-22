package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class HeartBeatAnimation implements CrateAnimation {

    private double scale = 1.0;
    private boolean growing = true;

    @Override
    public void play(Player player, Location location) {
        if (growing) {
            scale += 0.05;
            if (scale >= 1.3) growing = false;
        } else {
            scale -= 0.05;
            if (scale <= 1.0) growing = true;
        }

        Location base = location.clone().add(0, 1, 0);

        base.getWorld().spawnParticle(Particle.HEART, base.clone().add(scale * 0.2, 0, 0), 1);
        base.getWorld().spawnParticle(Particle.HEART, base.clone().add(-scale * 0.2, 0, 0), 1);
        base.getWorld().spawnParticle(Particle.HEART, base.clone().add(0, scale * 0.2, 0), 1);
        base.getWorld().spawnParticle(Particle.HEART, base.clone().add(scale * 0.15, -scale * 0.15, 0), 1);
        base.getWorld().spawnParticle(Particle.HEART, base.clone().add(-scale * 0.15, -scale * 0.15, 0), 1);
    }

    @Override
    public String getName() {
        return "heartbeat";
    }
}
