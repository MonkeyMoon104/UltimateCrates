package com.monkey.ultimateCrates.util;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.manager.CrateHologramManager;
import com.monkey.ultimateCrates.crates.model.Crate;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HologramSpawner {

    public static void spawnHologramAt(UltimateCrates plugin, CrateHologramManager hologramManager, Location loc, String crateId) {
        Optional<Crate> crateOpt = plugin.getCrateManager().getCrate(crateId);
        if (crateOpt.isEmpty()) {
            plugin.getLogger().warning("Crate con ID " + crateId + " non trovata per hologram.");
            return;
        }

        Crate crate = crateOpt.get();
        Location baseLoc = loc.clone().add(0.5, 1.5, 0.5);
        List<ArmorStand> stands = new ArrayList<>();

        for (int i = 0; i < crate.getHologramLines().size(); i++) {
            String line = crate.getHologramLines().get(i);
            Location lineLoc = baseLoc.clone().add(0, -0.25 * i, 0);

            ArmorStand stand = loc.getWorld().spawn(lineLoc, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setMarker(true);
                armorStand.setCustomNameVisible(true);
                armorStand.setCustomName(line.replace("&", "§"));
                armorStand.setGravity(false);
                armorStand.setSmall(true);
                armorStand.setInvulnerable(true);
                armorStand.setCollidable(false);
            });

            stands.add(stand);
        }

        hologramManager.registerHologram(loc, stands);
    }
}
