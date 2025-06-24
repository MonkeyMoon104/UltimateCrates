package com.monkey.ultimateCrates.events.listener;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.events.helper.StatsHuntEvent;
import com.monkey.ultimateCrates.events.handler.StatsHuntExecutor;
import com.monkey.ultimateCrates.util.AnimationUtils;
import com.monkey.ultimateCrates.util.KeyUtils;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class StatsHuntListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!(event.getAction().name().contains("RIGHT_CLICK") || event.getAction().name().contains("LEFT_CLICK")))
            return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.ENDER_CHEST) return;

        StatsHuntEvent currentEvent = StatsHuntExecutor.getCurrentEvent();
        Location expectedLocation = StatsHuntExecutor.getCurrentStatsHuntChestLocation();
        if (currentEvent == null || expectedLocation == null) return;

        Location clicked = clickedBlock.getLocation();
        if (!clicked.getWorld().equals(expectedLocation.getWorld()) ||
                clicked.getBlockX() != expectedLocation.getBlockX() ||
                clicked.getBlockY() != expectedLocation.getBlockY() ||
                clicked.getBlockZ() != expectedLocation.getBlockZ()) return;

        NBTBlock nbtBlock = new NBTBlock(clickedBlock);
        String crateId = nbtBlock.getData().getString("ultimatecrates_stats_event");
        if (crateId == null || crateId.isEmpty()) return;

        if (currentEvent.getCrateIds().stream().noneMatch(id -> id.equalsIgnoreCase(crateId))) return;

        event.setCancelled(true);
        clickedBlock.setType(Material.AIR);

        Player player = event.getPlayer();
        UltimateCrates plugin = UltimateCrates.getInstance();

        for (String id : currentEvent.getCrateIds()) {
            Optional<Crate> crateOpt = plugin.getCrateManager().getCrate(id);
            if (crateOpt.isEmpty()) continue;
            Crate crate = crateOpt.get();

            plugin.getDatabaseManager().getCrateStatisticStorage()
                    .incrementCrateOpen(player.getName(), id, currentEvent.getAmount());

            KeyUtils.rewardFunc(player, crate, currentEvent.getAmount());
        }

        String msg = plugin.getMessagesManager().getMessage("messages.events.statshunt.opened");
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        player.sendMessage(msg);

        AnimationUtils.playEventAnimations(plugin, player, "stats_hunt");

        StatsHuntExecutor.end(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.ENDER_CHEST) return;

        StatsHuntEvent currentEvent = StatsHuntExecutor.getCurrentEvent();
        Location expectedLocation = StatsHuntExecutor.getCurrentStatsHuntChestLocation();
        if (currentEvent == null || expectedLocation == null) return;

        Location broken = block.getLocation();
        if (!broken.getWorld().equals(expectedLocation.getWorld())) return;
        if (broken.getBlockX() != expectedLocation.getBlockX() ||
                broken.getBlockY() != expectedLocation.getBlockY() ||
                broken.getBlockZ() != expectedLocation.getBlockZ()) return;

        NBTBlock nbtBlock = new NBTBlock(block);
        String crateId = nbtBlock.getData().getString("ultimatecrates_stats_event");
        if (crateId == null || crateId.isEmpty()) return;

        if (currentEvent.getCrateIds().stream().anyMatch(id -> id.equalsIgnoreCase(crateId))) {
            event.setCancelled(true);
        }
    }
}
