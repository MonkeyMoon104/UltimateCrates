package com.monkey.ultimateCrates.crates.listener;

import com.monkey.ultimateCrates.UltimateCrates;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Event.Result;

public class CrateKeyInteractListener implements Listener {

    private final UltimateCrates plugin;

    public CrateKeyInteractListener(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasTag("crate_key")) {
            return;
        }

        if (action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            NBTBlock nbtBlock = new NBTBlock(event.getClickedBlock());
            if (!nbtBlock.getData().hasTag("crate_id")) {
                event.setCancelled(true);

                event.setUseItemInHand(Result.DENY);

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cNon è una crate!"));
                return;
            }
        }
    }
}
