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

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            if (handItem == null || handItem.getType() == Material.AIR) {
                player.sendMessage(ChatColor.RED + "Questa crate accetta solo chiavi fisiche. Devi avere una chiave fisica in mano.");
                return;
            }

            NBTItem nbtItem = new NBTItem(handItem);
            if (!nbtItem.hasTag("crate_key")) {
                player.sendMessage(ChatColor.RED + "Non puoi aprire questa crate senza una crate key fisica.");
                return;
            }

            String keyCrateId = nbtItem.getString("crate_key");
            if (!keyCrateId.equalsIgnoreCase(crate.getId())) {
                player.sendMessage(ChatColor.RED + "Questa chiave non appartiene a questa crate.");
                return;
            }

            if (handItem.getAmount() > 1) {
                handItem.setAmount(handItem.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }

            crateWinPrize(player, crate);
            return;
        } else if (crate.getKeyType() == Crate.KeyType.VIRTUAL) {
            if (handItem != null && handItem.getType() != Material.AIR) {
                player.sendMessage(ChatColor.RED + "Questa crate accetta solo chiavi virtuali. Non devi avere oggetti in mano.");
                return;
            }

            VirtualKeyStorage storage = plugin.getDatabaseManager().getVirtualKeyStorage();
            int keys = storage.getKeys(player.getName(), crate.getId());
            if (keys < 1) {
                player.sendMessage(ChatColor.RED + "Non hai abbastanza chiavi virtuali per aprire questa crate.");
                return;
            }

            storage.takeKeys(player.getName(), crate.getId(), 1);
            crateWinPrize(player, crate);
            return;
        }
    }

    private void crateWinPrize(Player player, Crate crate) {
        var prizes = crate.getPrizes();
        if (prizes.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Questa crate non contiene premi.");
            return;
        }

        ItemStack prize = prizes.get((int) (Math.random() * prizes.size()));
        player.getInventory().addItem(prize);

        String prizeName = prize.getItemMeta() != null && prize.getItemMeta().hasDisplayName()
                ? prize.getItemMeta().getDisplayName()
                : ChatColor.YELLOW + prize.getType().toString();
        player.sendMessage(ChatColor.GOLD + "Hai vinto: " + prizeName);

        var animationManager = plugin.getAnimationManager();
        var location = player.getLocation();

        for (String animationName : crate.getAnimationTemplates()) {
            animationManager.getAnimation(animationName).ifPresentOrElse(
                    animation -> animation.play(player, location),
                    () -> plugin.getLogger().warning("Animazione non trovata: " + animationName + " nella crate: " + crate.getId())
            );
        }
        plugin.getDatabaseManager().getCrateStatisticStorage().incrementCrateOpen(player.getName(), crate.getId());
    }

}
