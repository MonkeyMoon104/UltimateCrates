package com.monkey.ultimateCrates.events.handler.treasurehunt.helper.func;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.events.handler.treasurehunt.manager.TreasureHuntExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TreasureHuntEndHandler {

    public static void execute(boolean foundByPlayer) {
        TreasureHuntExecutor.clear();

        UltimateCrates plugin = UltimateCrates.getInstance();
        String key = foundByPlayer ? "messages.events.treasurehunt.found" : "messages.events.treasurehunt.ended";
        String msg = plugin.getMessagesManager().getMessage(key);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));

        Bukkit.getScheduler().runTask(plugin, () ->
                plugin.getCrateEventsManager().scheduleRandomEvent(plugin));
    }
}
