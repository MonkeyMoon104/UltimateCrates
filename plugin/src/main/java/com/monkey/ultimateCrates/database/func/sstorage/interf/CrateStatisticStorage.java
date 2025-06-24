package com.monkey.ultimateCrates.database.func.sstorage.interf;

import com.monkey.ultimateCrates.database.func.general.leaderboards.LeaderboardEntry;

import java.util.List;

public interface CrateStatisticStorage {

    void createTable();

    void incrementCrateOpen(String playerName, String crateId, int amount);

    int getCrateOpens(String playerName, String crateId);

    List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage);

    void resetPlayerStats(String playerName);

    void resetAllStats();

    boolean checkRewardAndReset(String playerName, String crateId, int rewardEvery, int amountAdded);
}
