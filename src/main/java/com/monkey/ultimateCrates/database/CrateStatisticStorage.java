package com.monkey.ultimateCrates.database;

import java.util.List;

public interface CrateStatisticStorage {

    void createTable();

    void incrementCrateOpen(String uuid, String crateId);

    int getCrateOpens(String uuid, String crateId);

    List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage);

    record LeaderboardEntry(String playerName, int amountOpened) {}
}
