package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.util.EffectUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BoltAnimation implements CrateAnimation {

    private final UltimateCrates plugin;

    public BoltAnimation(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public void play(Player player, Location loc) {
        loc.getWorld().strikeLightningEffect(loc);
        loc.getWorld().strikeLightningEffect(loc);
        loc.getWorld().strikeLightningEffect(loc);

        loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2f, 1f);

        EffectUtils.playRepeatingParticle(plugin, loc, Particle.CRIT, 150, 1.2, 1.5, 1.2, 0.2, 2L, 5);
    }

    @Override
    public String getName() {
        return "multi_bolt";
    }
}
