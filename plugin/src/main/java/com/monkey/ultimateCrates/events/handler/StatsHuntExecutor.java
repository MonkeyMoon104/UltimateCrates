package com.monkey.ultimateCrates.events.handler;

import com.monkey.ultimateCrates.events.StatsHuntEvent;
import org.bukkit.Bukkit;

public class StatsHuntExecutor {

    public static void start(StatsHuntEvent event) {
        Bukkit.getLogger().info("[UltimateCrates] Stats Hunt event started on block: " + event.getBlock());
    }
}
