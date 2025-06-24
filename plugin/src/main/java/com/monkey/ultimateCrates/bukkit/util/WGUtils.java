package com.monkey.ultimateCrates.bukkit.util;

import com.sk89q.worldguard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class WGUtils {

    private static boolean available = false;

    public static void setup() {
        Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
        available = wg != null && wg.isEnabled();
    }

    public static boolean isAvailable() {
        return available;
    }

    public static WorldGuard getWorldGuard() {
        return available ? WorldGuard.getInstance() : null;
    }
}
