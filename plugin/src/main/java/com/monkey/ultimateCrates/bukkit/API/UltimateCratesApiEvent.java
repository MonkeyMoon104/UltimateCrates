package com.monkey.ultimateCrates.bukkit.API;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.database.func.sstorage.interf.CrateStatisticStorage;
import com.monkey.ultimatecrates.api.UltimateCratesProvider;
import com.monkey.ultimatecrates.api.data.LeaderboardEntry;

import java.util.List;

public class UltimateCratesApiEvent implements UltimateCratesProvider {

    private final CrateStatisticStorage storage;

    public UltimateCratesApiEvent(UltimateCrates plugin) {
        this.storage = plugin.getDatabaseManager().getCrateStatisticStorage();
    }

    @Override
    public void incrementCrateOpen(String playerName, String crateId, int amount) {
        storage.incrementCrateOpen(playerName, crateId, amount);
    }

    @Override
    public int getCrateOpens(String playerName, String crateId) {
        return storage.getCrateOpens(playerName, crateId);
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage) {
        return storage.getLeaderboard(crateId, page, rowPerPage).stream()
                .map(entry -> new LeaderboardEntry(entry.playerName(), entry.amountOpened()))
                .toList();
    }

    @Override
    public void resetPlayerStats(String playerName) {
        storage.resetPlayerStats(playerName);
    }

    @Override
    public void resetAllStats() {
        storage.resetAllStats();
    }
}
