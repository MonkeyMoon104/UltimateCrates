package com.monkey.ultimateCrates.events.listener;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.events.handler.keyhunt.KeyHuntExecutor;
import com.monkey.ultimateCrates.events.logic.keyhunt.KeyHuntState;
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

public class KeyHuntListener implements Listener {

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        if (!(event.getAction().name().contains("RIGHT_CLICK") || event.getAction().name().contains("LEFT_CLICK")))
            return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.ENDER_CHEST)
            return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        Location clicked = clickedBlock.getLocation();
        Location active = KeyHuntState.getLocation();
        Crate crate = KeyHuntState.getCrate();

        if (active == null || crate == null) return;
        if (!clicked.getWorld().equals(active.getWorld())) return;
        if (clicked.getBlockX() != active.getBlockX() ||
                clicked.getBlockY() != active.getBlockY() ||
                clicked.getBlockZ() != active.getBlockZ()) return;

        NBTBlock nbtBlock = new NBTBlock(clickedBlock);
        String crateId = nbtBlock.getData().getString("ultimatecrates_keyhunt");
        if (crateId == null || !crate.getId().equalsIgnoreCase(crateId)) return;

        event.setCancelled(true);

        clickedBlock.setType(Material.AIR);

        int amount = KeyHuntState.getEvent().getAmount();
        UltimateCrates plugin = UltimateCrates.getInstance();

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            player.getInventory().addItem(KeyUtils.createPhysicalKey(crate, amount));
        } else if (crate.getKeyType() == Crate.KeyType.VIRTUAL) {
            plugin.getDatabaseManager().getVirtualKeyStorage().giveKeys(player.getName(), crate.getId(), amount);
        }

        String msg = plugin.getMessagesManager().getMessage("messages.events.keyhunt.pfound");
        msg = ChatColor.translateAlternateColorCodes('&', msg.replace("%keyname%", crate.getKeyName()));
        player.sendMessage(msg);

        AnimationUtils.playEventAnimationsOnChest(plugin, clickedBlock.getLocation(), "key_hunt");
        AnimationUtils.playEventAnimations(plugin, player, "key_hunt");

        KeyHuntExecutor.end(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.ENDER_CHEST) return;

        Location active = KeyHuntState.getLocation();
        if (active == null) return;

        Location broken = block.getLocation();
        if (!broken.getWorld().equals(active.getWorld())) return;
        if (broken.getBlockX() == active.getBlockX() &&
                broken.getBlockY() == active.getBlockY() &&
                broken.getBlockZ() == active.getBlockZ()) {
            event.setCancelled(true);
        }
    }
}
