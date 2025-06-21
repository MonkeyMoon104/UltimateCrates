package com.monkey.ultimateCrates.database.impl;

import com.monkey.ultimateCrates.database.CrateStatisticStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SQLiteCrateStatisticStorage implements CrateStatisticStorage {

    private final Connection connection;
    private final Logger logger;

    public SQLiteCrateStatisticStorage(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    @Override
    public void createTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS crate_stats (" +
                    "uuid TEXT NOT NULL, " +
                    "crate_id TEXT NOT NULL, " +
                    "amount_opened INTEGER NOT NULL DEFAULT 0, " +
                    "PRIMARY KEY (uuid, crate_id));");
        } catch (SQLException e) {
            logger.severe("Errore nella creazione della tabella crate_stats: " + e.getMessage());
        }
    }

    @Override
    public void incrementCrateOpen(String uuid, String crateId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO crate_stats (uuid, crate_id, amount_opened) VALUES (?, ?, 1) " +
                        "ON CONFLICT(uuid, crate_id) DO UPDATE SET amount_opened = amount_opened + 1"
        )) {
            stmt.setString(1, uuid);
            stmt.setString(2, crateId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Errore incrementando crate open: " + e.getMessage());
        }
    }

    @Override
    public int getCrateOpens(String uuid, String crateId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT amount_opened FROM crate_stats WHERE uuid = ? AND crate_id = ?"
        )) {
            stmt.setString(1, uuid);
            stmt.setString(2, crateId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("amount_opened");
                }
            }
        } catch (SQLException e) {
            logger.warning("Errore recuperando statistiche: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(String crateId, int page, int rowPerPage) {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        int offset = (page - 1) * rowPerPage;

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT uuid, amount_opened FROM crate_stats WHERE crate_id = ? " +
                        "ORDER BY amount_opened DESC LIMIT ? OFFSET ?"
        )) {
            stmt.setString(1, crateId);
            stmt.setInt(2, rowPerPage);
            stmt.setInt(3, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    int opened = rs.getInt("amount_opened");
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                    leaderboard.add(new LeaderboardEntry(player.getName(), opened));
                }
            }
        } catch (SQLException e) {
            logger.warning("Errore recuperando leaderboard: " + e.getMessage());
        }

        return leaderboard;
    }
}
