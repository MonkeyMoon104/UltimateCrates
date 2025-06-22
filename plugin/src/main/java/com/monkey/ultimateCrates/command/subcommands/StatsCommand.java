package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import com.monkey.ultimateCrates.database.func.sstorage.interf.CrateStatisticStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsCommand implements SubCommand {

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Mostra le statistiche delle crate aperte oppure resetta le statistiche";
    }

    @Override
    public String getSyntax() {
        return "/crate stats [player|reset <player>|resetall]";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public String getPermission() {
        return "uc.stats.use";
    }

    public String getResetPermission() {
        return "uc.admin.stats.reset";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UltimateCrates plugin = UltimateCrates.getInstance();
        CrateStatisticStorage statsStorage = plugin.getDatabaseManager().getCrateStatisticStorage();

        if (args.length >= 1 && args[0].equalsIgnoreCase("reset")) {
            if (!sender.hasPermission(getResetPermission())) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.no_permission"));
                return;
            }
            if (args.length == 2) {
                String targetPlayerName = args[1];
                statsStorage.resetPlayerStats(targetPlayerName);
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.stats.reset_player")
                        .replace("%player%", targetPlayerName));
                return;
            } else {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.stats.reset_specify_player"));
                return;
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("resetall")) {
            if (!sender.hasPermission(getResetPermission())) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.no_permission"));
                return;
            }
            statsStorage.resetAllStats();
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.stats.reset_all"));
            return;
        }

        String targetName;

        if (args.length == 1) {
            targetName = args[0];
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.stats.only_players"));
                return;
            }
            targetName = sender.getName();
        }

        plugin.getCrateManager().getCrates().forEach(crate -> {
            int opened = statsStorage.getCrateOpens(targetName, crate.getId());
            String crateName = ChatColor.translateAlternateColorCodes('&', crate.getDisplayName());
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.stats.crate_opens")
                    .replace("%crate%", crateName)
                    .replace("%amount%", String.valueOf(opened)));
        });
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> completions = new ArrayList<>();
            if ("reset".startsWith(partial)) completions.add("reset");
            if ("resetall".startsWith(partial)) completions.add("resetall");
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .forEach(completions::add);
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            String partial = args[1].toLowerCase();
            List<String> completions = new ArrayList<>();
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .forEach(completions::add);
            return completions;
        }
        return Collections.emptyList();
    }
}
