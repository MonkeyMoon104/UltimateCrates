package com.monkey.ultimateCrates.events.handler;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.events.helper.StatsHuntEvent;
import com.monkey.ultimateCrates.events.util.EventLocationUtils;
import com.monkey.ultimateCrates.events.util.RegionUtils;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.*;
import org.bukkit.block.Block;

public class StatsHuntExecutor {

    private static StatsHuntEvent currentEvent;
    private static boolean running = false;
    private static Location statsChestLocation = null;

    public static void start(StatsHuntEvent event, int durationMinutes) {
        if (running) return;

        UltimateCrates plugin = UltimateCrates.getInstance();
        World world = EventLocationUtils.getConfiguredWorld();

        if (event.getCrateIds().isEmpty()) {
            Bukkit.getLogger().warning("[UltimateCrates] StatsHuntEvent: Nessuna crateId specificata.");
            return;
        }

        Location location = null;
        for (int i = 0; i < 100; i++) {
            Location attempt = EventLocationUtils.getRandomLocationInsideWorldBorder(world);
            if (attempt != null && !RegionUtils.isInRegion(attempt)) {
                location = attempt;
                break;
            }
        }

        if (location == null) {
            Bukkit.getLogger().warning("[UltimateCrates] StatsHuntEvent: Nessuna location valida trovata per la StatsChest.");
            return;
        }

        Block block = location.getBlock();
        block.setType(Material.ENDER_CHEST);

        NBTBlock nbtBlock = new NBTBlock(block);
        String crateId = event.getCrateIds().getFirst();
        nbtBlock.getData().setString("ultimatecrates_stats_event", crateId);

        statsChestLocation = location;

        plugin.getEventsDatabaseFunctions().saveStatsHuntChest(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                crateId
        );

        currentEvent = event;
        running = true;

        String msg = plugin.getMessagesManager().getMessage("messages.events.statshunt.started");
        msg = ChatColor.translateAlternateColorCodes('&',
                msg.replace("%x%", String.valueOf(location.getBlockX()))
                        .replace("%y%", String.valueOf(location.getBlockY()))
                        .replace("%z%", String.valueOf(location.getBlockZ())));
        Bukkit.broadcastMessage(msg);

        Bukkit.getScheduler().runTaskLater(plugin, () -> end(false), durationMinutes * 60L * 20L);
    }

    public static void end(boolean foundByPlayer) {
        if (!running) return;
        clear();
        running = false;

        UltimateCrates plugin = UltimateCrates.getInstance();
        String msg;
        if (foundByPlayer) {
            msg = plugin.getMessagesManager().getMessage("messages.events.statshunt.found");
        } else {
            msg = plugin.getMessagesManager().getMessage("messages.events.statshunt.ended_no_find");
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));

        Bukkit.getScheduler().runTask(plugin, () -> {
            UltimateCrates.getInstance().getCrateEventsManager().scheduleRandomEvent(plugin);
        });
    }

    public static void clear() {
        if (statsChestLocation != null) {
            Block block = statsChestLocation.getBlock();
            if (block.getType() == Material.ENDER_CHEST) {
                block.setType(Material.AIR);
            }
        }
        statsChestLocation = null;
        UltimateCrates.getInstance().getEventsDatabaseFunctions().clearStatsHuntChests();
        currentEvent = null;
    }

    public static StatsHuntEvent getCurrentEvent() {
        return currentEvent;
    }

    public static boolean isRunning() {
        return running;
    }

    public static Location getCurrentStatsHuntChestLocation() {
        return statsChestLocation;
    }
}
