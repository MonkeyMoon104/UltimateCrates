package com.monkey.ultimateCrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand {
    String getName();
    String getDescription();
    String getSyntax();
    boolean onlyPlayers();

    void execute(CommandSender sender, String[] args);

    default List<String> tabComplete(Player player, String[] args) {
        return List.of();
    }
}
