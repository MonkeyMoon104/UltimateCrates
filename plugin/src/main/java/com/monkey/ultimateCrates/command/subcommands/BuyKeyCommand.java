package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import com.monkey.ultimateCrates.crates.manager.CratesManager;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.util.KeyUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BuyKeyCommand implements SubCommand {

    private final UltimateCrates plugin;
    private final CratesManager cratesManager;
    private final Economy economy;

    public BuyKeyCommand(UltimateCrates plugin) {
        this.plugin = plugin;
        this.cratesManager = plugin.getCrateManager();
        this.economy = plugin.getEconomy();
    }

    @Override
    public String getName() {
        return "buykey";
    }

    @Override
    public String getDescription() {
        return "Compra chiavi per una crate";
    }

    @Override
    public String getSyntax() {
        return "/crate buykey <crate> [amount]";
    }

    @Override
    public boolean onlyPlayers() {
        return true;
    }

    @Override
    public String getPermission() {
        return "uc.buykey.use";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.only_players"));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.usage"));
            return;
        }

        String crateId = args[0].toLowerCase();
        Optional<Crate> optionalCrate = cratesManager.getCrate(crateId);
        if (optionalCrate.isEmpty()) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.crate_not_found")
                    .replace("%crate%", crateId));
            return;
        }

        Crate crate = optionalCrate.get();

        if (!crate.canBuyEnabled()) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.disabled"));
            return;
        }

        int amount = 1;
        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.invalid_amount"));
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.invalid_amount"));
                return;
            }
        }

        if (economy == null) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.no_economy"));
            return;
        }

        double totalCost = crate.getCanBuyCost() * amount;

        if (!economy.has(player, totalCost)) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.not_enough_money"));
            return;
        }

        if (!economy.withdrawPlayer(player, totalCost).transactionSuccess()) {
            player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.transaction_failed"));
            return;
        }

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            ItemStack key = createPhysicalKey(crate, amount);
            player.getInventory().addItem(key);
        } else {
            try {
                plugin.getDatabaseManager().getVirtualKeyStorage().giveKeys(player.getName(), crate.getId(), amount);
            } catch (Exception e) {
                player.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.give_virtual_key_error"));
                plugin.getLogger().severe("Errore nel buykey virtuale: " + e.getMessage());
                return;
            }
        }

        String crateName = ChatColor.translateAlternateColorCodes('&', crate.getDisplayName());
        player.sendMessage(plugin.getMessagesManager().getMessage("messages.buykey.success")
                .replace("%amount%", String.valueOf(amount))
                .replace("%crate%", crateName)
                .replace("%price%", String.format("%.2f", totalCost)));
    }

    private ItemStack createPhysicalKey(Crate crate, int amount) {
        return KeyUtils.createPhysicalKey(crate, amount);
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return cratesManager.getCrates().stream()
                    .map(crate -> crate.getId().toLowerCase())
                    .filter(id -> id.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .toList();
        }

        return List.of();
    }
}
