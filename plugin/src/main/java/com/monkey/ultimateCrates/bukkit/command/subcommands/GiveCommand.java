package com.monkey.ultimateCrates.bukkit.command.subcommands;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.command.SubCommand;
import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GiveCommand implements SubCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Dai una crate a un giocatore";
    }

    @Override
    public String getSyntax() {
        return "/crate give <crateId> <player>";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public String getPermission() {
        return "uc.admin.give";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UltimateCrates plugin = UltimateCrates.getInstance();

        if (args.length < 2) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.give.usage_give"));
            return;
        }

        String crateId = args[0];
        Player target = plugin.getServer().getPlayerExact(args[1]);

        if (target == null) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.give.player_not_found"));
            return;
        }

        Optional<Crate> optionalCrate = plugin.getCrateManager().getCrate(crateId);
        if (optionalCrate.isEmpty()) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.give.crate_not_found")
                    .replace("%crate%", crateId));
            return;
        }

        Crate crate = optionalCrate.get();

        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', crate.getDisplayName()));

        List<String> coloredLore = crate.getHologramLines().stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .toList();

        meta.setLore(coloredLore);

        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("crate_id", crate.getId());

        item = nbtItem.getItem();

        target.getInventory().addItem(item);

        String crateName = ChatColor.translateAlternateColorCodes('&', crate.getDisplayName());

        sender.sendMessage(plugin.getMessagesManager()
                .getMessage("messages.give.give_success")
                .replace("%crate%", crateName)
                .replace("%player%", target.getName()));
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        UltimateCrates plugin = UltimateCrates.getInstance();

        if (args.length == 1) {
            return plugin.getCrateManager().getAllCrateIds().stream()
                    .filter(id -> id.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .map(org.bukkit.entity.Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
