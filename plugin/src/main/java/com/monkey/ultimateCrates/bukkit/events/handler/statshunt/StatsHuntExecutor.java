package com.monkey.ultimateCrates.bukkit.events.handler.statshunt;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.events.helper.StatsHuntEvent;
import com.monkey.ultimateCrates.bukkit.events.logic.statshunt.StatsHuntLocationPicker;
import com.monkey.ultimateCrates.bukkit.events.logic.statshunt.StatsHuntPersistence;
import com.monkey.ultimateCrates.bukkit.events.logic.statshunt.StatsHuntState;
import org.bukkit.*;
import org.bukkit.block.Block;

public class StatsHuntExecutor {

    public static void start(StatsHuntEvent event, int durationMinutes) {
        if (StatsHuntState.isRunning()) return;

        UltimateCrates plugin = UltimateCrates.getInstance();

        if (event.getCrateIds().isEmpty()) {
            plugin.getLogger().warning("[UltimateCrates] StatsHuntEvent: Nessuna crateId specificata.");
            return;
        }

        Location location = StatsHuntLocationPicker.pickValidLocation();
        if (location == null) {
            plugin.getLogger().warning("[UltimateCrates] StatsHuntEvent: Nessuna location valida trovata per la StatsChest.");
            return;
        }

        Block block = location.getBlock();
        block.setType(Material.ENDER_CHEST);

        String crateId = event.getCrateIds().getFirst();
        StatsHuntPersistence.markBlockWithNBT(block, crateId);
        StatsHuntState.set(event, location);
        StatsHuntPersistence.save(location, crateId);

        String msg = plugin.getMessagesManager().getMessage("messages.events.statshunt.started");
        msg = ChatColor.translateAlternateColorCodes('&',
                msg.replace("%x%", String.valueOf(location.getBlockX()))
                        .replace("%y%", String.valueOf(location.getBlockY()))
                        .replace("%z%", String.valueOf(location.getBlockZ())));
        Bukkit.broadcastMessage(msg);

        Bukkit.getScheduler().runTaskLater(plugin, () -> end(false), durationMinutes * 60L * 20L);
    }

    public static void end(boolean foundByPlayer) {
        if (!StatsHuntState.isRunning()) return;

        clear();
        StatsHuntState.setRunning(false);

        UltimateCrates plugin = UltimateCrates.getInstance();
        String key = foundByPlayer ? "messages.events.statshunt.found" : "messages.events.statshunt.ended_no_find";
        String msg = ChatColor.translateAlternateColorCodes('&', plugin.getMessagesManager().getMessage(key));
        Bukkit.broadcastMessage(msg);

        Bukkit.getScheduler().runTask(plugin, () -> plugin.getCrateEventsManager().scheduleRandomEvent(plugin));
    }

    public static void clear() {
        StatsHuntPersistence.clearBlock();
        StatsHuntState.reset();
    }
}
