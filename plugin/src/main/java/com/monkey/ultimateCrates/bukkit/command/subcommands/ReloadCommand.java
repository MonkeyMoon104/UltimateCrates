package com.monkey.ultimateCrates.bukkit.command.subcommands;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReloadCommand implements SubCommand {

    private final UltimateCrates plugin;

    public ReloadCommand(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Ricarica tutte le configurazioni e le crate.";
    }

    @Override
    public String getSyntax() {
        return "/crate reload";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public String getPermission() {
        return "uc.admin.reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.reload();
        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.command.reload_success"));
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }
}
