package com.monkey.ultimateCrates.bukkit.util;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.db.central.DatabaseCrates;

public class DBC {
    public static boolean init(DatabaseCrates databaseCrates) {
        try {
            databaseCrates.openConnection();
            return true;
        } catch (Exception e) {
            UltimateCrates.getInstance().getLogger().severe("Errore durante l'apertura del database delle crates:");
            e.printStackTrace();
            UltimateCrates.getInstance().getServer().getPluginManager().disablePlugin(UltimateCrates.getInstance());
            return false;
        }
    }
}
