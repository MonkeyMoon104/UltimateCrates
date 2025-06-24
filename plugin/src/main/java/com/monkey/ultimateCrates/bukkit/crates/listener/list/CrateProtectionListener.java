package com.monkey.ultimateCrates.bukkit.crates.listener.list;

import com.monkey.ultimateCrates.bukkit.crates.listener.helper.manager.CrateHologramManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;
import java.util.List;

public class CrateProtectionListener implements Listener {

    private final CrateHologramManager hologramManager;

    public CrateProtectionListener(CrateHologramManager hologramManager) {
        this.hologramManager = hologramManager;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        removeCratesFromExplosion(event.blockList());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        removeCratesFromExplosion(event.blockList());
    }

    private void removeCratesFromExplosion(List<Block> blocks) {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            Location location = block.getLocation();
            if (hologramManager.containsHologram(location)) {
                iterator.remove();
            }
        }
    }
}
