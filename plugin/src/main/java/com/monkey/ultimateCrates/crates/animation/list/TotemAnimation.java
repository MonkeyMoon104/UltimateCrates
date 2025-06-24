package com.monkey.ultimateCrates.crates.animation.list;

import com.monkey.ultimateCrates.crates.animation.inter.CrateAnimation;
import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.util.EffectUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TotemAnimation implements CrateAnimation {

    private final UltimateCrates plugin;

    public TotemAnimation(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public void play(Player player, Location loc) {
        loc.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 2.0f, 1.0f);
        EffectUtils.playRepeatingParticle(plugin, loc, Particle.TOTEM_OF_UNDYING, 300, 1.8, 2.5, 1.8, 0.3, 2L, 10);
    }

    @Override
    public String getName() {
        return "totem_explosion";
    }
}
