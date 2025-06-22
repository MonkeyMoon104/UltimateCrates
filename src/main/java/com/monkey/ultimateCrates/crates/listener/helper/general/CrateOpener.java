package com.monkey.ultimateCrates.crates.listener.helper.general;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.database.func.vkeys.interf.VirtualKeyStorage;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CrateOpener {

    private final UltimateCrates plugin;

    public CrateOpener(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void tryOpenCrate(Player player, String crateId) {
        Optional<Crate> crateOpt = plugin.getCrateManager().getCrate(crateId);
        if (!crateOpt.isPresent()) return;

        Crate crate = crateOpt.get();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            if (!hasValidPhysicalKey(handItem, crate, player)) return;

            consumePhysicalKey(handItem, player);
            crateWinPrize(player, crate);

        } else if (crate.getKeyType() == Crate.KeyType.VIRTUAL) {
            if (!hasValidVirtualKey(handItem, crate, player)) return;

            consumeVirtualKey(player, crate);
            crateWinPrize(player, crate);
        }
    }

    private boolean hasValidPhysicalKey(ItemStack handItem, Crate crate, Player player) {
        if (handItem == null || handItem.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "Questa crate accetta solo chiavi fisiche. Devi avere una chiave fisica in mano.");
            return false;
        }

        NBTItem nbtItem = new NBTItem(handItem);
        if (!nbtItem.hasTag("crate_key")) {
            player.sendMessage(ChatColor.RED + "Non puoi aprire questa crate senza una crate key fisica.");
            return false;
        }

        String keyCrateId = nbtItem.getString("crate_key");
        if (!keyCrateId.equalsIgnoreCase(crate.getId())) {
            player.sendMessage(ChatColor.RED + "Questa chiave non appartiene a questa crate.");
            return false;
        }

        return true;
    }

    private void consumePhysicalKey(ItemStack handItem, Player player) {
        if (handItem.getAmount() > 1) {
            handItem.setAmount(handItem.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }

    private boolean hasValidVirtualKey(ItemStack handItem, Crate crate, Player player) {
        if (handItem != null && handItem.getType() != Material.AIR) {
            player.sendMessage(ChatColor.RED + "Questa crate accetta solo chiavi virtuali. Non devi avere oggetti in mano.");
            return false;
        }

        VirtualKeyStorage storage = plugin.getDatabaseManager().getVirtualKeyStorage();
        int keys = storage.getKeys(player.getName(), crate.getId());
        if (keys < 1) {
            player.sendMessage(ChatColor.RED + "Non hai abbastanza chiavi virtuali per aprire questa crate.");
            return false;
        }
        return true;
    }

    private void consumeVirtualKey(Player player, Crate crate) {
        VirtualKeyStorage storage = plugin.getDatabaseManager().getVirtualKeyStorage();
        storage.takeKeys(player.getName(), crate.getId(), 1);
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

        int rewardEvery = crate.getRewardEvery();
        if (rewardEvery > 0) {
            boolean rewardGiven = plugin.getDatabaseManager().getCrateStatisticStorage()
                    .checkRewardAndReset(player.getName(), crate.getId(), rewardEvery);

            if (rewardGiven) {
                ItemStack rewardItem = parseRewardPrize(crate.getRewardItem(), crate.getRewardAmount());
                if (rewardItem != null) {
                    player.getInventory().addItem(rewardItem);
                    player.sendMessage(ChatColor.GOLD + "Hai ricevuto la ricompensa garantita per aver aperto " + rewardEvery + " crate " + crate.getDisplayName() + "!");
                }
            }
        }
    }

    private ItemStack parseRewardPrize(String materialName, int amount) {
        if (materialName == null) return null;

        Material material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) return null;

        return new ItemStack(material, amount);
    }
}
