package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class VirtualKeysCommand implements SubCommand {

    private final UltimateCrates plugin;

    public VirtualKeysCommand(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "virtualkeys";
    }

    @Override
    public String getDescription() {
        return "Mostra o resetta le chiavi virtuali";
    }

    @Override
    public String getSyntax() {
        return "/crate virtualkeys [player|reset <player>|resetall]";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("resetall")) {
            plugin.getDatabaseManager().getVirtualKeyStorage().resetAllKeys();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTutte le chiavi virtuali sono state resettate."));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            plugin.getDatabaseManager().getVirtualKeyStorage().resetKeys(target.getName());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChiavi virtuali resettate per &f" + target.getName()));
            return;
        }

        Player target;

        if (args.length >= 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGiocatore non trovato: " + args[0]));
                return;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDevi specificare un giocatore quando esegui questo comando dalla console."));
            return;
        }

        Map<String, Integer> allKeys = plugin.getDatabaseManager().getVirtualKeyStorage().getAllKeys(target.getName());

        if (allKeys.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + target.getName() + " non ha chiavi virtuali."));
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChiavi virtuali di &f" + target.getName() + "&a:"));

        boolean found = false;
        for (Map.Entry<String, Integer> entry : allKeys.entrySet()) {
            Optional<com.monkey.ultimateCrates.crates.model.Crate> crateOpt = plugin.getCrateManager().getCrate(entry.getKey());

            if (crateOpt.isPresent() && crateOpt.get().getKeyType() == com.monkey.ultimateCrates.crates.model.Crate.KeyType.VIRTUAL) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7- &e" + entry.getKey() + "&7: &b" + entry.getValue()));
                found = true;
            }
        }

        if (!found) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(Tutte le chiavi salvate appartengono a crate fisiche.)"));
        }
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("reset");
            suggestions.add("resetall");
            suggestions.addAll(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList());
            return suggestions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
