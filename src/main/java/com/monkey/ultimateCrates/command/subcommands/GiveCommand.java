package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import com.monkey.ultimateCrates.crates.model.Crate;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
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
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cUso corretto: /crate give <crateId> <giocatore>"));
            return;
        }

        String crateId = args[0];
        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cGiocatore non trovato."));
            return;
        }

        Optional<Crate> optionalCrate = UltimateCrates.getInstance().getCrateManager().getCrate(crateId);
        if (optionalCrate.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cCrate '" + crateId + "' non trovata."));
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
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHai dato una crate '" + crate.getDisplayName() + "' a " + target.getName()));
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return UltimateCrates.getInstance().getCrateManager().getAllCrateIds().stream()
                    .filter(id -> id.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
