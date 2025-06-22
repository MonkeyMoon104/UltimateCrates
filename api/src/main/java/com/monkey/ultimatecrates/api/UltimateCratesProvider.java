package com.monkey.ultimatecrates.api;

import com.monkey.ultimatecrates.api.data.LeaderboardEntry;

import java.util.List;

public interface UltimateCratesProvider {

    void incrementCrateOpen(String playerName, String crateId);

    int getCrateOpens(String playerName, String crateId);

    List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage);

    void resetPlayerStats(String playerName);

    void resetAllStats();
}
