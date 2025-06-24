package com.monkey.ultimateCrates.events.handler;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.manager.CrateHologramManager;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.crates.model.PCE;
import com.monkey.ultimateCrates.events.helper.TreasureHuntEvent;
import com.monkey.ultimateCrates.events.util.EventLocationUtils;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import java.sql.SQLException;
import java.util.*;

public class TreasureHuntExecutor {

    private static Location currentTreasureLocation;
    private static Crate currentTreasureCrate;
    private static TreasureHuntEvent currentTreasureEvent;
    private static boolean running = false;
    private static final List<ArmorStand> spawnedHolograms = new ArrayList<>();

    public static void start(TreasureHuntEvent event, int durationMinutes) {
        if (running) return;

        String crateId = event.getCrateId();
        UltimateCrates plugin = UltimateCrates.getInstance();

        Optional<Crate> optionalCrate = plugin.getCrateManager().getCrate(crateId);
        if (optionalCrate.isEmpty()) {
            plugin.getLogger().warning("TreasureHuntEvent: Crate '" + crateId + "' not found.");
            return;
        }

        Crate crate = optionalCrate.get();
        World world = EventLocationUtils.getConfiguredWorld();

        Location location = EventLocationUtils.getRandomSafePositionFromConfig();
        if (location == null) {
            plugin.getLogger().warning("TreasureHuntEvent: Nessuna posizione valida trovata nella configurazione.");
            return;
        }

        Block block = location.getBlock();
        block.setType(Material.CHEST);

        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setString("crate_id", crateId);
        nbtBlock.getData().setBoolean("crate_event", true);

        Location hologramBase = location.clone().add(0.5, 1.5, 0.5);
        List<String> lines = crate.getHologramLines();
        CrateHologramManager hologramManager = plugin.getCrateHologramManager();

        for (int i = 0; i < lines.size(); i++) {
            Location lineLoc = hologramBase.clone().add(0, -0.25 * i, 0);
            int finalI = i;
            ArmorStand stand = location.getWorld().spawn(lineLoc, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setMarker(true);
                armorStand.setCustomNameVisible(true);
                armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', lines.get(finalI)));
                armorStand.setGravity(false);
                armorStand.setSmall(true);
            });
            spawnedHolograms.add(stand);
        }

        hologramManager.registerHologram(location, spawnedHolograms);

        try {
            plugin.getDatabaseCrates().savePlacedCrate(location, crateId);
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore salvataggio crate DB: " + ex.getMessage());
        }

        plugin.getEventsDatabaseFunctions().saveTreasureHuntChest(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                crateId
        );

        PCE effectConfig = crate.getParticleEffectConfig();
        if (effectConfig != null && effectConfig.isEnabled()) {
            plugin.getParticlesManager().spawnFixedEffectAt(location, effectConfig.getType(), effectConfig.getEffect());
        }

        currentTreasureLocation = location;
        currentTreasureCrate = crate;
        currentTreasureEvent = event;
        running = true;

        String msg = plugin.getMessagesManager().getMessage("messages.events.treasurehunt.started");
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
            msg = plugin.getMessagesManager().getMessage("messages.events.treasurehunt.found");
        } else {
            msg = plugin.getMessagesManager().getMessage("messages.events.treasurehunt.ended");
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));

        Bukkit.getScheduler().runTask(plugin, () -> {
            plugin.getCrateEventsManager().scheduleRandomEvent(plugin);
        });
    }

    public static void clear() {
        if (currentTreasureLocation != null) {
            Block block = currentTreasureLocation.getBlock();
            if (block.getType() == Material.CHEST) {
                block.setType(Material.AIR);
            }
        }

        try {
            UltimateCrates.getInstance().getDatabaseCrates().removePlacedCrate(currentTreasureLocation);
        } catch (SQLException e) {
            UltimateCrates.getInstance().getLogger().severe("Errore durante la rimozione della crate nel DB (clear Treasure): " + e.getMessage());
        }

        for (ArmorStand stand : spawnedHolograms) {
            stand.remove();
        }
        spawnedHolograms.clear();

        UltimateCrates.getInstance().getCrateHologramManager().removeHologram(currentTreasureLocation);
        UltimateCrates.getInstance().getParticlesManager().removeEffectAt(currentTreasureLocation);

        UltimateCrates.getInstance().getEventsDatabaseFunctions().clearTreasureHuntChest();

        currentTreasureLocation = null;
        currentTreasureCrate = null;
        currentTreasureEvent = null;
    }

    public static Location getCurrentTreasureLocation() {
        return currentTreasureLocation;
    }

    public static Crate getCurrentTreasureCrate() {
        return currentTreasureCrate;
    }

    public static boolean isRunning() {
        return running;
    }

    public static TreasureHuntEvent getCurrentTreasureEvent() {
        return currentTreasureEvent;
    }
}
