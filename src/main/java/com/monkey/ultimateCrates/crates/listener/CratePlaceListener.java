package com.monkey.ultimateCrates.crates.listener;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.crates.model.ParticleEffectConfig;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;

public class CratePlaceListener implements Listener {

    private final Map<Location, List<ArmorStand>> holograms = new HashMap<>();

    @EventHandler
    public void onCratePlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasTag("crate_id")) return;

        String crateId = nbtItem.getString("crate_id");
        Crate crate = UltimateCrates.getInstance().getCrateManager().getCrate(crateId).orElse(null);
        if (crate == null) return;

        Location blockLoc = event.getBlockPlaced().getLocation();

        for (Location loc : holograms.keySet()) {
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

        holograms.put(blockLoc, stands);

        try {
            UltimateCrates.getInstance().getDatabaseCrates().savePlacedCrate(blockLoc, crateId);
        } catch (SQLException ex) {
            UltimateCrates.getInstance().getLogger().severe("Errore nel salvataggio crate nel DB: " + ex.getMessage());
        }

        ParticleEffectConfig effectConfig = crate.getParticleEffectConfig();

        if (effectConfig != null && effectConfig.isEnabled()) {
            String effect = effectConfig.getType();
            String style = effectConfig.getEffect();

            UltimateCrates.getInstance().getParticlesManager()
                    .spawnFixedEffectAt(blockLoc, effect, style);
        }
    }

    @EventHandler
    public void onCrateBreakAttempt(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!event.getPlayer().isSneaking()) return;

        Location blockLoc = event.getClickedBlock().getLocation();
        if (!holograms.containsKey(blockLoc)) return;

        event.setCancelled(true);

        blockLoc.getBlock().setType(Material.AIR);

        List<ArmorStand> stands = holograms.remove(blockLoc);
        if (stands != null) {
            stands.forEach(stand -> {
                if (!stand.isDead()) stand.remove();
            });
        }

        UltimateCrates.getInstance().getParticlesManager().removeEffectAt(blockLoc);

        try {
            UltimateCrates.getInstance().getDatabaseCrates().removePlacedCrate(blockLoc);
        } catch (SQLException ex) {
            UltimateCrates.getInstance().getLogger().severe("Errore nella rimozione crate dal DB: " + ex.getMessage());
        }

        try {
            UltimateCrates.getInstance().getDatabaseCrates().removeFixedParticle(blockLoc);
        } catch (SQLException ex) {
            UltimateCrates.getInstance().getLogger().severe("Errore nella rimozione dell'effetto dal DB: " + ex.getMessage());
        }

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cHai rimosso una crate."));
    }

    @EventHandler
    public void onCrateBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        if (!holograms.containsKey(loc)) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cPer rimuovere una crate, fai Shift + Click Destro."));
    }

    public Map<Location, List<ArmorStand>> getHolograms() {
        return holograms;
    }

    public void registerHologram(Location location, List<ArmorStand> stands) {
        holograms.put(location, stands);
    }
}
