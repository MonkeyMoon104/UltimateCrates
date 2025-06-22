package com.monkey.ultimateCrates.util;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.db.central.DatabaseCrates;

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
