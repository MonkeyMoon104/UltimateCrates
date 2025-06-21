package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.command.CommandManager;
import com.monkey.ultimateCrates.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Mostra la lista dei comandi disponibili";
    }

    @Override
    public String getSyntax() {
        return "/crate help";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8&m+-----------------------------+"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&'," &eUltimateCrates &7- &6Comandi disponibili:"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&'," &e/crate givekey <player> <crate> <amount>"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&'," &e/crate open <crate>"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&'," &e/crate stats"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&'," &e/crate help"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8&m+-----------------------------+"));
    }
}
