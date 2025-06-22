package com.monkey.ultimateCrates.command.subcommands;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.command.SubCommand;
import com.monkey.ultimateCrates.database.func.general.leaderboards.LeaderboardEntry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TopCommand implements SubCommand {

    private final UltimateCrates plugin;

    public TopCommand(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String getDescription() {
        return "Mostra la classifica casse aperte per un crate specifico";
    }

    @Override
    public String getSyntax() {
        return "/crate top <crateId> [pagina]";
    }

    @Override
    public boolean onlyPlayers() {
        return false;
    }

    @Override
    public String getPermission() {
        return "uc.top.use";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.top.usage"));
            return;
        }

        String crateId = args[0];
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1) page = 1;
            } catch (NumberFormatException ignored) {}
        }

        if (plugin.getCrateManager().getCrate(crateId).isEmpty()) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.crate.not_found").replace("%crate%", crateId));
            return;
        }

        int rowsPerPage = 10;
        List<LeaderboardEntry> leaderboard = plugin.getDatabaseManager().getCrateStatisticStorage().getLeaderboard(crateId, page, rowsPerPage);

        if (leaderboard.isEmpty()) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("messages.top.empty"));
            return;
        }

        sender.sendMessage(plugin.getMessagesManager().getMessage("messages.top.header")
                .replace("%page%", String.valueOf(page))
                .replace("%crate%", crateId));

        int rankStart = (page - 1) * rowsPerPage + 1;
        for (int i = 0; i < leaderboard.size(); i++) {
            LeaderboardEntry entry = leaderboard.get(i);
            String line = plugin.getMessagesManager().getMessage("messages.top.line")
                    .replace("%rank%", String.valueOf(rankStart + i))
                    .replace("%player%", entry.playerName())
                    .replace("%amount%", String.valueOf(entry.amountOpened()));
            sender.sendMessage(line);
        }
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return plugin.getCrateManager().getAllCrateIds();
        }
        return List.of();
    }
}
