package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.crates.listener.helper.manager.CrateHologramManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CrateBreakListener implements Listener {

    private final CrateHologramManager hologramManager;

    public CrateBreakListener(CrateHologramManager hologramManager) {
        this.hologramManager = hologramManager;
    }

    @EventHandler
    public void onCrateBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        if (!hologramManager.containsHologram(loc)) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cPer rimuovere una crate, fai Shift + Click Destro."));
    }
}
