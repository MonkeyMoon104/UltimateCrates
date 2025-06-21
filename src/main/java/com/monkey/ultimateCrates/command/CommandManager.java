package com.monkey.ultimateCrates.command;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public CommandManager() {
        register(new HelpCommand(this));
        register(new GiveKeyCommand(UltimateCrates.getInstance()));
        register(new VirtualKeysCommand(UltimateCrates.getInstance()));
        register(new GiveCommand());
        register(new StatsCommand());
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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cComando sconosciuto. Usa /crate help"));
            return true;
        }

        if (cmd.onlyPlayers() && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cSolo i giocatori possono usare questo comando."));
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
