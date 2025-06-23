package com.monkey.ultimateCrates.events.handler;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.events.KeyHuntEvent;
import com.monkey.ultimateCrates.events.db.func.EventsDBFunctions;
import com.monkey.ultimateCrates.events.util.EventLocationUtils;
import com.monkey.ultimateCrates.events.util.RegionUtils;
import com.monkey.ultimateCrates.util.KeyUtils;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class KeyHuntExecutor {

    private static Location currentKeyHuntChestLocation;
    private static Crate currentKeyHuntCrate;
    private static boolean running = false;

    private static final EventsDBFunctions dbFunctions = new EventsDBFunctions();

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

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            BlockState state = block.getState();
            if (state instanceof Container container) {
                Inventory inv = container.getInventory();
                ItemStack key = KeyUtils.createPhysicalKey(crate, 1);
                for (int i = 0; i < inv.getSize(); i++) {
                    inv.setItem(i, key.clone());
                }
            }
        }

        currentKeyHuntChestLocation = location;
        currentKeyHuntCrate = crate;
        running = true;

        dbFunctions.saveKeyHuntChest(world.getName(),
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
        dbFunctions.clearKeyHuntChest();
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
}
