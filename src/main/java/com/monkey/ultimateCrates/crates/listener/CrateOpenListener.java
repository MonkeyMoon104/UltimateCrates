package com.monkey.ultimateCrates.crates.listener;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.database.impl.VirtualKeyStorage;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CrateOpenListener implements Listener {

    private final UltimateCrates plugin;

    public CrateOpenListener(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCrateUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().isSneaking()) return;

        Player player = event.getPlayer();
        NBTBlock nbtBlock = new NBTBlock(event.getClickedBlock());

        if (!nbtBlock.getData().hasTag("crate_id")) return;

        event.setCancelled(true);

        String crateId = nbtBlock.getData().getString("crate_id");
        Optional<Crate> crateOpt = plugin.getCrateManager().getCrate(crateId);
        if (!crateOpt.isPresent()) return;

        Crate crate = crateOpt.get();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem == null || handItem.getType() == Material.AIR) {
            switch (crate.getKeyType()) {
                case PHYSIC -> {
                    player.sendMessage(ChatColor.RED + "Questa crate accetta solo chiavi fisiche.");
                }
                case VIRTUAL -> {
                    VirtualKeyStorage storage = plugin.getDatabaseManager().getVirtualKeyStorage();
                    int keys = storage.getKeys(player.getUniqueId(), crate.getId());
                    if (keys < 1) {
                        player.sendMessage(ChatColor.RED + "Non hai abbastanza chiavi virtuali per aprire questa crate.");
                        return;
                    }

                    storage.takeKeys(player.getUniqueId(), crate.getId(), 1);

                    crateWinPrize(player, crate);
                }
            }
            return;
        }

        NBTItem nbtItem = new NBTItem(handItem);
        if (!nbtItem.hasTag("crate_key")) {
            player.sendMessage(ChatColor.RED + "Non puoi aprire questa crate senza una crate key.");
        }
    }

    private void crateWinPrize(Player player, Crate crate) {
        player.sendMessage(ChatColor.GREEN + "Hai aperto la crate virtuale: " + crate.getDisplayName());
    }
}
