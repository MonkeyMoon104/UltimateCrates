package com.monkey.ultimateCrates.events.handler;


import com.monkey.ultimateCrates.events.TreasureHuntEvent;
import org.bukkit.Bukkit;

public class TreasureHuntExecutor {

    public static void start(TreasureHuntEvent event) {
        String crateId = event.getCrateId();
        Bukkit.getLogger().info("[UltimateCrates] Treasure Hunt event started with crate: " + crateId);

    }
}
