package com.monkey.ultimateCrates.database.impl;

import com.monkey.ultimateCrates.database.CrateStatisticStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLCrateStatisticStorage implements CrateStatisticStorage {

    private final Connection connection;
    private final Logger logger;

    public MySQLCrateStatisticStorage(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    @Override
    public void createTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS crate_stats (" +
                    "player_name VARCHAR(32) NOT NULL, " +
                    "crate_id VARCHAR(64) NOT NULL, " +
                    "amount_opened INT NOT NULL DEFAULT 0, " +
                    "reward_counter INT NOT NULL DEFAULT 0, " +
                    "PRIMARY KEY (player_name, crate_id));");
        } catch (SQLException e) {
            logger.severe("Errore nella creazione della tabella crate_stats (MySQL): " + e.getMessage());
        }
    }

    @Override
    public void incrementCrateOpen(String playerName, String crateId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO crate_stats (player_name, crate_id, amount_opened) VALUES (?, ?, 1) " +
                        "ON DUPLICATE KEY UPDATE amount_opened = amount_opened + 1"
        )) {
            stmt.setString(1, playerName);
            stmt.setString(2, crateId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Errore incrementando crate open (MySQL): " + e.getMessage());
        }
    }

    @Override
    public int getCrateOpens(String playerName, String crateId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT amount_opened FROM crate_stats WHERE player_name = ? AND crate_id = ?"
        )) {
            stmt.setString(1, playerName);
            stmt.setString(2, crateId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("amount_opened");
                }
            }
        } catch (SQLException e) {
            logger.warning("Errore recuperando statistiche (MySQL): " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void resetPlayerStats(String playerName) {
        String sql = "DELETE FROM crate_stats WHERE player_name = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerName);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore resettando stats del giocatore MySQL", e);
        }
    }

    @Override
    public void resetAllStats() {
        String sql = "DELETE FROM crate_stats";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore resettando tutte le stats MySQL", e);
        }
    }

    @Override
    public boolean checkRewardAndReset(String playerName, String crateId, int rewardEvery) {
        try (PreparedStatement stmtInsertOrUpdate = connection.prepareStatement(
                "INSERT INTO crate_stats (player_name, crate_id, amount_opened, reward_counter) " +
                        "VALUES (?, ?, 0, 1) " +
                        "ON DUPLICATE KEY UPDATE reward_counter = reward_counter + 1"
        )) {
            stmtInsertOrUpdate.setString(1, playerName);
            stmtInsertOrUpdate.setString(2, crateId);
            stmtInsertOrUpdate.executeUpdate();

            try (PreparedStatement stmtSelect = connection.prepareStatement(
                    "SELECT reward_counter FROM crate_stats WHERE player_name = ? AND crate_id = ?"
            )) {
                stmtSelect.setString(1, playerName);
                stmtSelect.setString(2, crateId);

                try (ResultSet rs = stmtSelect.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt("reward_counter");
                        if (count >= rewardEvery) {
                            try (PreparedStatement stmtReset = connection.prepareStatement(
                                    "UPDATE crate_stats SET reward_counter = 0 WHERE player_name = ? AND crate_id = ?"
                            )) {
                                stmtReset.setString(1, playerName);
                                stmtReset.setString(2, crateId);
                                stmtReset.executeUpdate();
                            }
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.warning("Errore in checkRewardAndReset (MySQL): " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage) {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        int offset = (page - 1) * rowPerPage;

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT player_name, amount_opened FROM crate_stats WHERE crate_id = ? " +
                        "ORDER BY amount_opened DESC LIMIT ? OFFSET ?"
        )) {
            stmt.setString(1, crateId);
            stmt.setInt(2, rowPerPage);
            stmt.setInt(3, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String playerName = rs.getString("player_name");
                    int opened = rs.getInt("amount_opened");
                    leaderboard.add(new LeaderboardEntry(playerName, opened));
                }
            }
        } catch (SQLException e) {
            logger.warning("Errore recuperando leaderboard (MySQL): " + e.getMessage());
        }

        return leaderboard;
    }
}
