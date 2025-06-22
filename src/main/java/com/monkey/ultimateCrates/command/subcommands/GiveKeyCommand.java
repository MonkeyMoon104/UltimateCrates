package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import com.monkey.ultimateCrates.crates.manager.CratesManager;
import com.monkey.ultimateCrates.crates.model.Crate;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GiveKeyCommand implements SubCommand {

    private final UltimateCrates plugin;
    private final CratesManager cratesManager;

    public GiveKeyCommand(UltimateCrates plugin) {
        this.plugin = plugin;
        this.cratesManager = plugin.getCrateManager();
    }

    @Override
    public String getName() {
        return "givekey";
    }

    @Override
    public String getDescription() {
        return "Dà una chiave per una crate";
    }

    @Override
    public String getSyntax() {
        return "/crate givekey <crate> [player] [amount]";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public String getPermission() {
        return "uc.admin.givekey";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.usage_givekey").replace("%syntax%", getSyntax()));
            return;
        }

        String crateId = args[0].toLowerCase();
        Optional<Crate> optionalCrate = cratesManager.getCrate(crateId);
        if (optionalCrate.isEmpty()) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.crate_not_found").replace("%crate%", crateId));
            return;
        }
        Crate crate = optionalCrate.get();

        Player target;
        int amount = 1;

        if (args.length >= 2) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.player_not_found_named").replace("%player%", args[1]));
                return;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.player_required_console"));
            return;
        }

        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
                if (amount <= 0) {
                    sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.amount_positive"));
                    return;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.amount_invalid"));
                return;
            }
        }

        if (crate.getKeyType() == Crate.KeyType.PHYSIC) {
            ItemStack key = createPhysicalKey(crate, amount);
            target.getInventory().addItem(key);
            String crateName = ChatColor.translateAlternateColorCodes('&', crate.getDisplayName());
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.give_physical_key_sender")
                    .replace("%amount%", String.valueOf(amount))
                    .replace("%crate%", crateName)
                    .replace("%player%", target.getName()));
            if (!target.equals(sender)) {
                target.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.give_physical_key_target")
                        .replace("%amount%", String.valueOf(amount))
                        .replace("%crate%", crateName));
            }
        } else {
            try {
                String crateName = ChatColor.translateAlternateColorCodes('&', crate.getDisplayName());
                plugin.getDatabaseManager().getVirtualKeyStorage().giveKeys(target.getName(), crate.getId(), amount);
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.give_virtual_key_sender")
                        .replace("%amount%", String.valueOf(amount))
                        .replace("%crate%", crateName)
                        .replace("%player%", target.getName()));

                int totalKeys = plugin.getDatabaseManager().getVirtualKeyStorage().getKeys(target.getName(), crate.getId());

                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.virtual_key_total")
                        .replace("%player%", target.getName())
                        .replace("%total%", String.valueOf(totalKeys))
                        .replace("%crate%", crateName));

                if (!target.equals(sender)) {
                    target.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.give_virtual_key_target")
                            .replace("%amount%", String.valueOf(amount))
                            .replace("%crate%", crateName));
                }
            } catch (Exception e) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.givekey.give_virtual_key_error"));
                plugin.getLogger().severe("Errore nel givekey virtuale: " + e.getMessage());
            }
        }
    }

    private ItemStack createPhysicalKey(Crate crate, int amount) {
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK, amount);

        NBTItem nbtKey = new NBTItem(key);
        nbtKey.setString("crate_key", crate.getId());

        var meta = nbtKey.getItem().getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', crate.getKeyName()));

            if (crate.isEnchanted()) {
                meta.addEnchant(Enchantment.MENDING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            nbtKey.getItem().setItemMeta(meta);
        }

        return nbtKey.getItem();
    }


    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return cratesManager.getAllCrateIds().stream()
                    .filter(id -> id.startsWith(args[0].toLowerCase()))
                    .toList();
        } else if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return Collections.emptyList();
    }
}
