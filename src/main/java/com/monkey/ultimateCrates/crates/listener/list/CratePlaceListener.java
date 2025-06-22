package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.manager.CrateHologramManager;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.crates.model.PCE;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CratePlaceListener implements Listener {

    private final UltimateCrates plugin;
    private final CrateHologramManager hologramManager;

    public CratePlaceListener(UltimateCrates plugin, CrateHologramManager hologramManager) {
        this.plugin = plugin;
        this.hologramManager = hologramManager;
    }

    @EventHandler
    public void onCratePlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasTag("crate_id")) return;

        String crateId = nbtItem.getString("crate_id");
        Crate crate = plugin.getCrateManager().getCrate(crateId).orElse(null);
        if (crate == null) return;

        Location blockLoc = event.getBlockPlaced().getLocation();

        for (Location loc : hologramManager.getHolograms().keySet()) {
            if (loc.getWorld().equals(blockLoc.getWorld()) && loc.distance(blockLoc) < 1.5) {
                player.sendMessage(ChatColor.RED + "Non puoi posizionare una crate così vicina ad un'altra!");
                event.setCancelled(true);
                return;
            }
        }

        NBTBlock nbtBlock = new NBTBlock(event.getBlockPlaced());
        nbtBlock.getData().setString("crate_id", crateId);

        Location hologramBase = blockLoc.clone().add(0.5, 1.5, 0.5);
        List<ArmorStand> stands = new ArrayList<>();

        for (int i = 0; i < crate.getHologramLines().size(); i++) {
            String line = crate.getHologramLines().get(i);
            Location lineLoc = hologramBase.clone().add(0, -0.25 * i, 0);
            ArmorStand stand = player.getWorld().spawn(lineLoc, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setMarker(true);
                armorStand.setCustomNameVisible(true);
                armorStand.setCustomName(line.replace("&", "§"));
                armorStand.setGravity(false);
                armorStand.setSmall(true);
            });
            stands.add(stand);
        }

        hologramManager.registerHologram(blockLoc, stands);

        try {
            plugin.getDatabaseCrates().savePlacedCrate(blockLoc, crateId);
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore nel salvataggio crate nel DB: " + ex.getMessage());
        }

        PCE effectConfig = crate.getParticleEffectConfig();

        if (effectConfig != null && effectConfig.isEnabled()) {
            String effect = effectConfig.getType();
            String style = effectConfig.getEffect();

            plugin.getParticlesManager().spawnFixedEffectAt(blockLoc, effect, style);
        }
    }
}
