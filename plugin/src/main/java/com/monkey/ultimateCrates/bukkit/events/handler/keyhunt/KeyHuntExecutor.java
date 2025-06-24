package com.monkey.ultimateCrates.bukkit.events.handler.keyhunt;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import com.monkey.ultimateCrates.bukkit.events.helper.KeyHuntEvent;
import com.monkey.ultimateCrates.bukkit.events.logic.keyhunt.KeyHuntLocationPicker;
import com.monkey.ultimateCrates.bukkit.events.logic.keyhunt.KeyHuntPersistence;
import com.monkey.ultimateCrates.bukkit.events.logic.keyhunt.KeyHuntState;
import com.monkey.ultimateCrates.bukkit.proxy.ProxyMSG;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Optional;

public class KeyHuntExecutor {

    public static void start(KeyHuntEvent event, int durationMinutes) {
        if (KeyHuntState.isRunning()) return;

        UltimateCrates plugin = UltimateCrates.getInstance();
        Optional<Crate> optionalCrate = plugin.getCrateManager().getCrate(event.getKeyName());

        if (optionalCrate.isEmpty()) {
            plugin.getLogger().warning("[UltimateCrates] KeyHuntEvent: Crate '" + event.getKeyName() + "' not found.");
            return;
        }

        Crate crate = optionalCrate.get();
        Location location = KeyHuntLocationPicker.pickValidLocation(crate);

        if (location == null) {
            plugin.getLogger().warning("[UltimateCrates] KeyHuntEvent: Nessuna posizione valida trovata.");
            return;
        }

        Block block = location.getBlock();
        block.setType(Material.ENDER_CHEST);

        KeyHuntPersistence.markBlockWithNBT(block, crate);
        KeyHuntState.set(crate, event, location);
        KeyHuntPersistence.save(location, crate);

        String spawnMsg = plugin.getMessagesManager().getMessage("messages.events.keyhunt.spawned")
                .replace("%x%", String.valueOf(location.getBlockX()))
                .replace("%y%", String.valueOf(location.getBlockY()))
                .replace("%z%", String.valueOf(location.getBlockZ()));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', spawnMsg));

        Bukkit.getScheduler().runTaskLater(plugin, () -> end(false), durationMinutes * 60L * 20L);
    }

    public static void end(boolean foundByPlayer) {
        if (!KeyHuntState.isRunning()) return;
        UltimateCrates plugin = UltimateCrates.getInstance();

        clear();
        KeyHuntState.setRunning(false);

        String msgKey = foundByPlayer ? "messages.events.keyhunt.bcfound" : "messages.events.keyhunt.ended";
        String broadcastMsg = plugin.getMessagesManager().getMessage(msgKey);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMsg));

        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getCrateEventsManager().scheduleRandomEvent(plugin);
        });
    }

    public static void clear() {
        KeyHuntPersistence.clearBlock();
        KeyHuntState.reset();
    }
}
