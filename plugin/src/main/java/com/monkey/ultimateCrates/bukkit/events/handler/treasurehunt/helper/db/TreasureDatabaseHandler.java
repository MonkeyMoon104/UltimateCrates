package com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.db;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import org.bukkit.Location;

public class TreasureDatabaseHandler {

    public static void save(Location location, String crateId) {
        UltimateCrates plugin = UltimateCrates.getInstance();

        try {
            plugin.getDatabaseCrates().savePlacedCrate(location, crateId);
        } catch (Exception e) {
            plugin.getLogger().severe("Errore salvataggio crate DB: " + e.getMessage());
        }

        plugin.getEventsDatabaseFunctions().saveTreasureHuntChest(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                crateId
        );
    }
}
