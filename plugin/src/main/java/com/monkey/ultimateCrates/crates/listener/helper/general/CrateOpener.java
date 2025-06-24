package com.monkey.ultimateCrates.crates.listener.helper.general;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.crates.model.CratePrize;
import com.monkey.ultimateCrates.crates.util.CratePrizeSelector;
import com.monkey.ultimateCrates.database.func.vkeys.interf.VirtualKeyStorage;
import com.monkey.ultimateCrates.events.handler.treasurehunt.manager.TreasureHuntExecutor;
import com.monkey.ultimateCrates.util.AnimationUtils;
import com.monkey.ultimateCrates.util.KeyUtils;
import de.tr7zw.nbtapi.NBTItem;
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
        if (crateOpt.isEmpty()) return;

        Crate crate = crateOpt.get();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            if (!hasValidPhysicalKey(handItem, crate, player)) return;

            consumePhysicalKey(handItem, player);
            crateWinPrize(player, crate, false, null);

        } else if (crate.getKeyType() == Crate.KeyType.VIRTUAL) {
            if (!hasValidVirtualKey(handItem, crate, player)) return;

            consumeVirtualKey(player, crate);
            crateWinPrize(player, crate, false, null);
        }
    }

    public void tryOpenEventCrate(Player player, String crateId) {
        Optional<Crate> crateOpt = plugin.getCrateManager().getCrate(crateId);
        if (crateOpt.isEmpty()) return;

        Crate crate = crateOpt.get();

        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            if (!hasValidPhysicalKey(handItem, crate, player)) return;

            consumePhysicalKey(handItem, player);
            crateWinPrize(player, crate, true, "treasure_hunt");

        } else if (crate.getKeyType() == Crate.KeyType.VIRTUAL) {
            if (!hasValidVirtualKey(handItem, crate, player)) return;

            consumeVirtualKey(player, crate);
            crateWinPrize(player, crate, true, "treasure_hunt");
        }

        TreasureHuntExecutor.end(true);
    }


    private boolean hasValidPhysicalKey(ItemStack handItem, Crate crate, Player player) {
        if (handItem == null || handItem.getType() == Material.AIR) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.only_physical_key_in_hand"));
            return false;
        }

        NBTItem nbtItem = new NBTItem(handItem);
        if (!nbtItem.hasTag("crate_key")) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.must_have_physical_key"));
            return false;
        }

        String keyCrateId = nbtItem.getString("crate_key");
        if (!keyCrateId.equalsIgnoreCase(crate.getId())) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.key_not_belong_to_crate"));
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
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.no_item_in_hand_for_virtual_key"));
            return false;
        }

        VirtualKeyStorage storage = plugin.getDatabaseManager().getVirtualKeyStorage();
        int keys = storage.getKeys(player.getName(), crate.getId());
        if (keys < 1) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.not_enough_virtual_keys"));
            return false;
        }
        return true;
    }

    private void consumeVirtualKey(Player player, Crate crate) {
        VirtualKeyStorage storage = plugin.getDatabaseManager().getVirtualKeyStorage();
        storage.takeKeys(player.getName(), crate.getId(), 1);
    }

    private void crateWinPrize(Player player, Crate crate, boolean event, String eventName) {
        var prizes = crate.getPrizes();
        if (prizes.isEmpty()) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.no_prizes"));
            return;
        }

        CratePrize selected = CratePrizeSelector.selectPrize(prizes);
        if (selected == null) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.no_prizes"));
            return;
        }

        ItemStack prize = selected.getItem();

        player.getInventory().addItem(prize);

        String prizeName = prize.getItemMeta() != null && prize.getItemMeta().hasDisplayName()
                ? prize.getItemMeta().getDisplayName()
                : plugin.getMessagesManager().getMessage("messages.crate.default_prize_name", prize.getType().toString());

        player.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.win_prize", prizeName));

        if (!event) {
            AnimationUtils.playCrateAnimations(UltimateCrates.getInstance(), player, crate);
        } else {
            AnimationUtils.playEventAnimations(UltimateCrates.getInstance(), player, eventName);
        }
        plugin.getDatabaseManager().getCrateStatisticStorage().incrementCrateOpen(player.getName(), crate.getId(), 1);

        KeyUtils.rewardFunc(player, crate, 1);

    }
}
