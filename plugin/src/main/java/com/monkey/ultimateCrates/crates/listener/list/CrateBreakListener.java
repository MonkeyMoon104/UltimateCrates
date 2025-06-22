package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.crates.listener.helper.manager.CrateHologramManager;
import com.monkey.ultimateCrates.UltimateCrates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CrateBreakListener implements Listener {

    private final CrateHologramManager hologramManager;
    private final UltimateCrates plugin;

    public CrateBreakListener(CrateHologramManager hologramManager, UltimateCrates plugin) {
        this.hologramManager = hologramManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrateBreak(BlockBreakEvent event) {
        if (!hologramManager.containsHologram(event.getBlock().getLocation())) return;

        if (!event.getPlayer().hasPermission("uc.admin.break")) {
            event.setCancelled(true);
            String message = plugin.getMessagesManager().getMessage("messages.command.no_permission");
            event.getPlayer().sendMessage(message);
            return;
        }

        event.setCancelled(true);
        String message = plugin.getMessagesManager().getMessage("messages.crate.break_hint");
        event.getPlayer().sendMessage(message);
    }
}
