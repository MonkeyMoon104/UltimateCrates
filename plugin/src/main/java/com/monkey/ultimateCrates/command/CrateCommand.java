package com.monkey.ultimateCrates.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class CrateCommand implements CommandExecutor, TabCompleter {

    private final CommandManager commandManager = new CommandManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandManager.execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return sender instanceof Player ? commandManager.tabComplete((Player) sender, args) : List.of();
    }
}
