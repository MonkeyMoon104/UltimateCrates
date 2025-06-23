package com.monkey.ultimateCrates.events.util;

import com.monkey.ultimateCrates.util.WGUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class RegionUtils {

    public static boolean isInRegion(Location location) {
        if (!WGUtils.isAvailable()) return false;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(location.getWorld());
        RegionManager manager = container.get(adaptedWorld);
        if (manager == null) return false;

        com.sk89q.worldedit.util.Location weLocation = BukkitAdapter.adapt(location);
        ApplicableRegionSet set = manager.getApplicableRegions(weLocation.toVector().toBlockPoint());

        return set.size() > 0;
    }

    public static boolean isWorldGuardPresent() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        return plugin instanceof WorldGuardPlugin;
    }
}
