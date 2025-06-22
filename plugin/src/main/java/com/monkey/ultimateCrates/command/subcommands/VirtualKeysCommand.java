package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import com.monkey.ultimateCrates.crates.model.Crate;
import org.bukkit.Bukkit;
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
    public String getPermission() {
        return "uc.vkeys.use";
    }

    public String getResetPermission() {
        return "uc.admin.vkeys.reset";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("resetall")) {
            if (!sender.hasPermission(getResetPermission())) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.no_permission"));
                return;
            }
            plugin.getDatabaseManager().getVirtualKeyStorage().resetAllKeys();
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.reset_all"));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            if (!sender.hasPermission(getResetPermission())) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.no_permission"));
                return;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            plugin.getDatabaseManager().getVirtualKeyStorage().resetKeys(target.getName());
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.reset_player")
                    .replace("%player%", target.getName()));
            return;
        }

        Player target;

        if (args.length >= 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.player_not_found")
                        .replace("%player%", args[0]));
                return;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.specify_player_console"));
            return;
        }

        Map<String, Integer> allKeys = plugin.getDatabaseManager().getVirtualKeyStorage().getAllKeys(target.getName());

        if (allKeys.isEmpty()) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.no_keys")
                    .replace("%player%", target.getName()));
            return;
        }

        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.list_header")
                .replace("%player%", target.getName()));

        boolean found = false;
        for (Map.Entry<String, Integer> entry : allKeys.entrySet()) {
            Optional<Crate> crateOpt = plugin.getCrateManager().getCrate(entry.getKey());

            if (crateOpt.isPresent() && crateOpt.get().getKeyType() == Crate.KeyType.VIRTUAL) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.list_entry")
                        .replace("%crate%", entry.getKey())
                        .replace("%amount%", String.valueOf(entry.getValue())));
                found = true;
            }
        }

        if (!found) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.virtualkeys.no_virtual_keys"));
        }
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("reset");
            suggestions.add("resetall");
            Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .forEach(suggestions::add);
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
