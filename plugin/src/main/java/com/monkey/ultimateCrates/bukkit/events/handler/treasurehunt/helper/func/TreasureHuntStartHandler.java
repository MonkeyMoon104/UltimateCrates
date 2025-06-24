package com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.func;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import com.monkey.ultimateCrates.bukkit.crates.model.PCE;
import com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.db.TreasureDatabaseHandler;
import com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.holo.TreasureHologramHandler;
import com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.manager.TreasureHuntExecutor;
import com.monkey.ultimateCrates.bukkit.events.helper.TreasureHuntEvent;
import com.monkey.ultimateCrates.bukkit.events.util.EventLocationUtils;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.Optional;

public class TreasureHuntStartHandler {

    public static void execute(TreasureHuntEvent event, int durationMinutes) {
        String crateId = event.getCrateId();
        UltimateCrates plugin = UltimateCrates.getInstance();

        Optional<Crate> optionalCrate = plugin.getCrateManager().getCrate(crateId);
        if (optionalCrate.isEmpty()) {
            plugin.getLogger().warning("TreasureHuntEvent: Crate '" + crateId + "' not found.");
            return;
        }

        Crate crate = optionalCrate.get();
        Location location = EventLocationUtils.getRandomSafePositionFromConfig();

        if (location == null) {
            plugin.getLogger().warning("TreasureHuntEvent: Nessuna posizione valida trovata nella configurazione.");
            return;
        }

        placeTreasureChest(location, crateId);
        TreasureHologramHandler.spawnHolograms(location, crate);
        TreasureDatabaseHandler.save(location, crateId);

        PCE effectConfig = crate.getParticleEffectConfig();
        if (effectConfig != null && effectConfig.isEnabled()) {
            plugin.getParticlesManager().spawnFixedEffectAt(location, effectConfig.getType(), effectConfig.getEffect());
        }

        TreasureHuntExecutor.setCurrentState(location, crate, event);

        String msg = plugin.getMessagesManager().getMessage("messages.events.treasurehunt.started")
                .replace("%x%", String.valueOf(location.getBlockX()))
                .replace("%y%", String.valueOf(location.getBlockY()))
                .replace("%z%", String.valueOf(location.getBlockZ()));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));

        Bukkit.getScheduler().runTaskLater(plugin, () -> TreasureHuntExecutor.end(false), durationMinutes * 60L * 20L);
    }

    private static void placeTreasureChest(Location location, String crateId) {
        Block block = location.getBlock();
        block.setType(Material.CHEST);
        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setString("crate_id", crateId);
        nbtBlock.getData().setBoolean("crate_event", true);
    }
}
