package com.monkey.ultimateCrates.bukkit.setup;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.listener.helper.manager.CrateHologramManager;
import com.monkey.ultimateCrates.bukkit.util.HologramSpawner;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.Map;

public class SpawnedHolograms {

    public static void loadAllFromDatabase(UltimateCrates plugin, CrateHologramManager hologramManager) {
        try {
            Map<Location, String> loadedCrates = plugin.getDatabaseCrates().loadAllPlacedCrates();

            for (Map.Entry<Location, String> entry : loadedCrates.entrySet()) {
                HologramSpawner.spawnHologramAt(plugin, hologramManager, entry.getKey(), entry.getValue());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Errore caricando crate dal DB!");
        }
    }
}
