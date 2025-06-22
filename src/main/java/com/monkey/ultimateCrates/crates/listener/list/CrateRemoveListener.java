package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.manager.CrateHologramManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.sql.SQLException;
import java.util.List;

public class CrateRemoveListener implements Listener {

    private final UltimateCrates plugin;
    private final CrateHologramManager hologramManager;

    public CrateRemoveListener(UltimateCrates plugin, CrateHologramManager hologramManager) {
        this.plugin = plugin;
        this.hologramManager = hologramManager;
    }

    @EventHandler
    public void onCrateBreakAttempt(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!event.getPlayer().isSneaking()) return;

        if (!event.getPlayer().hasPermission("uc.admin.break")) {
            event.setCancelled(true);
            String message = plugin.getMessagesManager().getMessage("messages.command.no_permission");
            event.getPlayer().sendMessage(message);
            return;
        }

        Location blockLoc = event.getClickedBlock().getLocation();
        if (!hologramManager.containsHologram(blockLoc)) return;

        event.setCancelled(true);

        blockLoc.getBlock().setType(Material.AIR);

        List<ArmorStand> stands = hologramManager.removeHologram(blockLoc);
        if (stands != null) {
            stands.forEach(stand -> {
                if (!stand.isDead()) stand.remove();
            });
        }

        plugin.getParticlesManager().removeEffectAt(blockLoc);

        try {
            plugin.getDatabaseCrates().removePlacedCrate(blockLoc);
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore nella rimozione crate dal DB: " + ex.getMessage());
        }

        try {
            plugin.getDatabaseCrates().removeFixedParticle(blockLoc);
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore nella rimozione dell'effetto dal DB: " + ex.getMessage());
        }

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cHai rimosso una crate."));
    }
}
