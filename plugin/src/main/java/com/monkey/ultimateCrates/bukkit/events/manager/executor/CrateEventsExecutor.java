package com.monkey.ultimateCrates.bukkit.events.manager.executor;

import com.monkey.ultimateCrates.bukkit.events.handler.keyhunt.KeyHuntExecutor;
import com.monkey.ultimateCrates.bukkit.events.handler.statshunt.StatsHuntExecutor;
import com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.manager.TreasureHuntExecutor;
import com.monkey.ultimateCrates.bukkit.events.manager.data.CrateEventsData;
import com.monkey.ultimateCrates.bukkit.proxy.ProxyMSG;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CrateEventsExecutor {

    public static void executeRandomEvent(CrateEventsData data, Plugin plugin) {
        String selected = data.getRandomSelectedEvent().orElse(null);
        if (selected == null) return;

        Bukkit.getLogger().info("[UltimateCrates] Avvio evento: " + selected);

        boolean proxyEnabled = plugin.getConfig().getBoolean("proxy.enabled", false);
        if (proxyEnabled) {
            Player player = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
            if (player != null) {
                String duration = String.valueOf(data.getDelayMinutes());
                String serverName = plugin.getConfig().getString("proxy.server-name", "unknown");
                new ProxyMSG(plugin).sendEventNotifyToProxy(player, selected, duration, serverName);
            }
        }


        switch (selected) {
            case "key_hunt" -> data.getKeyHuntEvent().ifPresent(event -> KeyHuntExecutor.start(event, data.getDelayMinutes()));
            case "treasure_hunt" -> data.getTreasureHuntEvent().ifPresent(event -> TreasureHuntExecutor.start(event, data.getDelayMinutes()));
            case "stats_hunt" -> data.getStatsHuntEvent().ifPresent(event -> StatsHuntExecutor.start(event, data.getDelayMinutes()));
        }
    }
}
