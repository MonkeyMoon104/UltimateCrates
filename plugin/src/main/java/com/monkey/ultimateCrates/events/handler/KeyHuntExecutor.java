package com.monkey.ultimateCrates.events.handler;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.events.KeyHuntEvent;
import com.monkey.ultimateCrates.events.db.func.EventsDBFunctions;
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

        String keyName = event.getKeyName();
        UltimateCrates plugin = UltimateCrates.getInstance();

        Optional<Crate> optionalCrate = plugin.getCrateManager().getCrate(keyName);
        if (optionalCrate.isEmpty()) {
            Bukkit.getLogger().warning("[UltimateCrates] KeyHuntEvent: Crate '" + keyName + "' not found.");
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
            Bukkit.getLogger().warning("[UltimateCrates] KeyHuntEvent: Nessuna posizione valida trovata.");
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

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "&6[UltimateCrates] &eUna &dEnderChest &eè apparsa alle coordinate &b" +
                        location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "&e!"));

        Bukkit.getScheduler().runTaskLater(UltimateCrates.getInstance(), () -> {
            end(false);
        }, durationMinutes * 60L * 20L);
    }

    public static void end(boolean foundByPlayer) {
        if (!running) return;

        clear();
        running = false;

        if (foundByPlayer) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&6[UltimateCrates] &aLa &dchiave misteriosa &aè stata trovata!"));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&6[UltimateCrates] &eL'evento &dKey Hunt &eè terminato!"));
        }
        Bukkit.getScheduler().runTask(UltimateCrates.getInstance(), () -> {
            UltimateCrates.getInstance().getCrateEventsManager().scheduleRandomEvent(UltimateCrates.getInstance());
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
