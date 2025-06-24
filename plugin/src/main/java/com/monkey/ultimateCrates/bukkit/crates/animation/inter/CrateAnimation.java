package com.monkey.ultimateCrates.bukkit.crates.animation.inter;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface CrateAnimation {
    void play(Player player, Location location);
    String getName();
}
