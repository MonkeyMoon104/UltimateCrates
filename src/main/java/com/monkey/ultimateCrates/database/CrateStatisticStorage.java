package com.monkey.ultimateCrates.database;

import java.util.List;

public interface CrateStatisticStorage {

    void createTable();

    void incrementCrateOpen(String playerName, String crateId);

    int getCrateOpens(String playerName, String crateId);

    List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage);

    void resetPlayerStats(String playerName);

    void resetAllStats();

    boolean checkRewardAndReset(String playerName, String crateId, int rewardEvery);

    record LeaderboardEntry(String playerName, int amountOpened) {}
}
