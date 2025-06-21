package com.monkey.ultimateCrates.database.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLCrateOpenStorage implements CrateOpenStorage {

    private final Connection connection;
    private final Logger logger;

    public MySQLCrateOpenStorage(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    @Override
    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS crate_opens (
                player_uuid VARCHAR(36) NOT NULL,
                crate_id VARCHAR(64) NOT NULL,
                open_count INT NOT NULL DEFAULT 0,
                PRIMARY KEY (player_uuid, crate_id)
            ) ENGINE=InnoDB;
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nella creazione tabella crate_opens MySQL", e);
        }
    }

    @Override
    public void addOpen(UUID playerUUID, String crateId) {
        String sql = """
            INSERT INTO crate_opens (player_uuid, crate_id, open_count)
            VALUES (?, ?, 1)
            ON DUPLICATE KEY UPDATE open_count = open_count + 1
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, crateId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nell'aggiunta open_count MySQL", e);
        }
    }

    @Override
    public int getOpenCount(UUID playerUUID, String crateId) {
        String sql = "SELECT open_count FROM crate_opens WHERE player_uuid = ? AND crate_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, crateId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("open_count");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nel recupero open_count MySQL", e);
        }
        return 0;
    }
}
