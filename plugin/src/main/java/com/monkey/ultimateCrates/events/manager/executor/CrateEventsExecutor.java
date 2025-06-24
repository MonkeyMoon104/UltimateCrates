package com.monkey.ultimateCrates.events.manager.executor;

import com.monkey.ultimateCrates.events.handler.keyhunt.KeyHuntExecutor;
import com.monkey.ultimateCrates.events.handler.statshunt.StatsHuntExecutor;
import com.monkey.ultimateCrates.events.handler.treasurehunt.manager.TreasureHuntExecutor;
import com.monkey.ultimateCrates.events.manager.data.CrateEventsData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class CrateEventsExecutor {

    public static void executeRandomEvent(CrateEventsData data, Plugin plugin) {
        String selected = data.getRandomSelectedEvent().orElse(null);
        if (selected == null) return;

        Bukkit.getLogger().info("[UltimateCrates] Avvio evento: " + selected);

        switch (selected) {
            case "key_hunt" -> data.getKeyHuntEvent().ifPresent(event -> KeyHuntExecutor.start(event, data.getDelayMinutes()));
            case "treasure_hunt" -> data.getTreasureHuntEvent().ifPresent(event -> TreasureHuntExecutor.start(event, data.getDelayMinutes()));
            case "stats_hunt" -> data.getStatsHuntEvent().ifPresent(event -> StatsHuntExecutor.start(event, data.getDelayMinutes()));
        }
    }
}
