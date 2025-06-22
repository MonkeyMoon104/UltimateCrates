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
    public void execute(CommandSender sender, String[] args) {
        UltimateCrates plugin = UltimateCrates.getInstance();
        CrateStatisticStorage statsStorage = plugin.getDatabaseManager().getCrateStatisticStorage();

        if (args.length >= 1 && args[0].equalsIgnoreCase("reset")) {

            if (args.length == 2) {
                String targetPlayerName = args[1];
                statsStorage.resetPlayerStats(targetPlayerName);
                sender.sendMessage(ChatColor.GREEN + "Statistiche resettate per il giocatore " + targetPlayerName);
                return;
            } else {
                sender.sendMessage(ChatColor.RED + "Specifica un giocatore o usa 'resetall'.");
                return;
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("resetall")) {
            statsStorage.resetAllStats();
            sender.sendMessage(ChatColor.GREEN + "Tutte le statistiche sono state resettate.");
            return;
        }

        String targetName;

        if (args.length == 1) {
            targetName = args[0];
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Solo i giocatori possono vedere le proprie statistiche senza argomenti.");
                return;
            }
            targetName = sender.getName();
        }

        plugin.getCrateManager().getCrates().forEach(crate -> {
            int opened = statsStorage.getCrateOpens(targetName, crate.getId());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7- &6" + crate.getDisplayName() + "&7: &b" + opened + " aperture"));
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
