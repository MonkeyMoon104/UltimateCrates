package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.command.CommandManager;
import com.monkey.ultimateCrates.command.SubCommand;
import com.monkey.ultimateCrates.UltimateCrates;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {

    private final CommandManager manager;
    private final UltimateCrates plugin;

    public HelpCommand(CommandManager manager, UltimateCrates plugin) {
        this.manager = manager;
        this.plugin = plugin;
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
    public String getPermission() {
        return "uc.help.use";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.header"));
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.command_givekey"));
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.command_give"));
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.command_stats"));
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.command_vkey"));
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.command_reload"));
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.command_help"));
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.help.footer"));
    }
}
