package com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.holo;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

public class TreasureHologramHandler {

    private static final List<ArmorStand> spawnedHolograms = new ArrayList<>();

    public static void spawnHolograms(Location baseLocation, Crate crate) {
        Location hologramBase = baseLocation.clone().add(0.5, 1.5, 0.5);
        List<String> lines = crate.getHologramLines();

        for (int i = 0; i < lines.size(); i++) {
            Location lineLoc = hologramBase.clone().add(0, -0.25 * i, 0);
            int finalI = i;
            ArmorStand stand = baseLocation.getWorld().spawn(lineLoc, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setMarker(true);
                armorStand.setCustomNameVisible(true);
                armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', lines.get(finalI)));
                armorStand.setGravity(false);
                armorStand.setSmall(true);
            });
            spawnedHolograms.add(stand);
        }

        UltimateCrates.getInstance().getCrateHologramManager().registerHologram(baseLocation, spawnedHolograms);
    }

    public static List<ArmorStand> getSpawnedHolograms() {
        return spawnedHolograms;
    }

    public static void clear() {
        spawnedHolograms.clear();
    }
}
