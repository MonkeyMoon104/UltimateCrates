package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements SubCommand {

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Mostra le statistiche delle crate aperte";
    }

    @Override
    public String getSyntax() {
        return "/crate stats";
    }

    @Override
    public boolean onlyPlayers() {
        return true;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        UltimateCrates plugin = UltimateCrates.getInstance();
        plugin.getCrateManager().getCrates().forEach(crate -> {
            int opened = plugin.getDatabaseManager().getCrateOpenStorage().getOpenCount(player.getUniqueId(), crate.getId());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7- &6" + crate.getDisplayName() + "&7: &b" + opened + " aperture"));
        });
    }
}
