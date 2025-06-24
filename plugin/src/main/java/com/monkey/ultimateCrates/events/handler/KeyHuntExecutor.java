package com.monkey.ultimateCrates.events.handler;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.events.helper.KeyHuntEvent;
import com.monkey.ultimateCrates.events.util.EventLocationUtils;
import com.monkey.ultimateCrates.events.util.RegionUtils;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.Optional;

public class KeyHuntExecutor {

    private static Location currentKeyHuntChestLocation;
    private static Crate currentKeyHuntCrate;
    private static boolean running = false;
    private static KeyHuntEvent currentKeyHuntEvent;

    public static void start(KeyHuntEvent event, int durationMinutes) {
        if (running) return;

        UltimateCrates plugin = UltimateCrates.getInstance();

        Optional<Crate> optionalCrate = plugin.getCrateManager().getCrate(event.getKeyName());
        if (optionalCrate.isEmpty()) {
            plugin.getLogger().warning("[UltimateCrates] KeyHuntEvent: Crate '" + event.getKeyName() + "' not found.");
            return;
        }

        Crate crate = optionalCrate.get();
        World world = EventLocationUtils.getConfiguredWorld();

        Location location = null;
        for (int i = 0; i < 100; i++) {
            Location attempt = EventLocationUtils.getRandomLocationInsideWorldBorder(world);
            if (attempt != null && !RegionUtils.isInRegion(attempt)) {
                location = attempt;
                break;
            }
        }

        if (location == null) {
            plugin.getLogger().warning("[UltimateCrates] KeyHuntEvent: Nessuna posizione valida trovata.");
            return;
        }

        Block block = location.getBlock();
        block.setType(Material.ENDER_CHEST);

        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setString("ultimatecrates_keyhunt", crate.getId());

        currentKeyHuntChestLocation = location;
        currentKeyHuntCrate = crate;
        running = true;
        currentKeyHuntEvent = event;

        plugin.getEventsDatabaseFunctions().saveKeyHuntChest(world.getName(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                crate.getId());

        String spawnMsg = plugin.getMessagesManager().getMessage("messages.events.keyhunt.spawned")
                .replace("%x%", String.valueOf(location.getBlockX()))
                .replace("%y%", String.valueOf(location.getBlockY()))
                .replace("%z%", String.valueOf(location.getBlockZ()));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', spawnMsg));

        Bukkit.getScheduler().runTaskLater(plugin, () -> end(false), durationMinutes * 60L * 20L);
    }

    public static void end(boolean foundByPlayer) {
        if (!running) return;

        clear();
        running = false;

        UltimateCrates plugin = UltimateCrates.getInstance();
        String msgKey = foundByPlayer ? "messages.events.keyhunt.bcfound" : "messages.events.keyhunt.ended";

        String broadcastMsg = plugin.getMessagesManager().getMessage(msgKey);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMsg));

        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getCrateEventsManager().scheduleRandomEvent(plugin);
        });
    }

    public static void clear() {
        if (currentKeyHuntChestLocation != null) {
            Block block = currentKeyHuntChestLocation.getBlock();
            if (block.getType() == Material.ENDER_CHEST) {
                block.setType(Material.AIR);
            }
        }

        currentKeyHuntChestLocation = null;
        currentKeyHuntCrate = null;
        currentKeyHuntEvent = null;
        UltimateCrates.getInstance().getEventsDatabaseFunctions().clearKeyHuntChest();
    }

    public static Location getCurrentKeyHuntChestLocation() {
        return currentKeyHuntChestLocation;
    }

    public static Crate getCurrentKeyHuntCrate() {
        return currentKeyHuntCrate;
    }

    public static boolean isRunning() {
        return running;
    }

    public static KeyHuntEvent getCurrentKeyHuntEvent() {
        return currentKeyHuntEvent;
    }
}
