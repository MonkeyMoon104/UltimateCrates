package com.monkey.ultimateCrates.crates.listener.helper.manager;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateHologramManager {

    private static final Map<Location, List<ArmorStand>> holograms = new HashMap<>();

    public static Map<Location, List<ArmorStand>> getHolograms() {
        return holograms;
    }

    public void registerHologram(Location location, List<ArmorStand> stands) {
        holograms.put(location, stands);
    }

    public List<ArmorStand> removeHologram(Location location) {
        return holograms.remove(location);
    }

    public boolean containsHologram(Location location) {
        return holograms.containsKey(location);
    }
}
