package com.monkey.ultimateCrates.placeholder;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.database.CrateStatisticStorage;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class UCPlaceholder extends PlaceholderExpansion {

    private final UltimateCrates plugin;

    public UCPlaceholder(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "ultimatecrates";
    }

    @Override
    public String getAuthor() {
        return "MonkeyMoon104";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.startsWith("leaderboard_")) {
            String[] parts = identifier.split("_");
            if (parts.length < 4) return null;

            String crateId = parts[1];
            int page = Integer.parseInt(parts[2]);
            int row = Integer.parseInt(parts[3]);

            var leaderboard = plugin.getDatabaseManager().getCrateStatisticStorage().getLeaderboard(crateId, page, row);
            if (row - 1 < leaderboard.size()) {
                CrateStatisticStorage.LeaderboardEntry entry = leaderboard.get(row - 1);
                return entry.playerName() + ": " + entry.amountOpened();
            } else {
                return "N/A";
            }
        }
        return null;
    }
}
