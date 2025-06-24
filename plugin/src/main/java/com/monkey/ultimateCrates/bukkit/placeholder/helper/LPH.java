package com.monkey.ultimateCrates.bukkit.placeholder.helper;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.database.func.general.leaderboards.LeaderboardEntry;

public class LPH {

    public static String handle(UltimateCrates plugin, String identifier) {
        String[] parts = identifier.split("_");
        if (parts.length < 4) return null;

        String crateId = parts[1];
        int page;
        int row;

        try {
            page = Integer.parseInt(parts[2]);
            row = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            return "N/A";
        }

        var leaderboard = plugin.getDatabaseManager().getCrateStatisticStorage().getLeaderboard(crateId, page, row);
        if (row - 1 < leaderboard.size()) {
            LeaderboardEntry entry = leaderboard.get(row - 1);
            return entry.playerName() + ": " + entry.amountOpened();
        }

        return "N/A";
    }
}
