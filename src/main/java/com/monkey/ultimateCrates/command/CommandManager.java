package com.monkey.ultimateCrates.command;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.subcommands.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager {

    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final UltimateCrates plugin = UltimateCrates.getInstance();

    public CommandManager() {
        register(new HelpCommand(this, UltimateCrates.getInstance()));
        register(new GiveKeyCommand(plugin));
        register(new VirtualKeysCommand(plugin));
        register(new GiveCommand());
        register(new StatsCommand());
        register(new ReloadCommand(plugin));
    }

    private void register(SubCommand command) {
        subCommands.put(command.getName().toLowerCase(), command);
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            subCommands.get("help").execute(sender, args);
            return true;
        }

        SubCommand cmd = subCommands.get(args[0].toLowerCase());
        if (cmd == null) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.unknown_command"));
            return true;
        }

        if (cmd.onlyPlayers() && !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.only_players"));
            return true;
        }

        if (!sender.hasPermission(cmd.getPermission())) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.no_permission"));
            return true;
        }


        cmd.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .toList();
        }

        SubCommand cmd = subCommands.get(args[0].toLowerCase());
        return cmd != null ? cmd.tabComplete(player, Arrays.copyOfRange(args, 1, args.length)) : List.of();
    }
}
