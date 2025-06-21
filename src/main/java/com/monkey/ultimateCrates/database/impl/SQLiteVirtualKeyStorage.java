package com.monkey.ultimateCrates.database.impl;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteVirtualKeyStorage implements VirtualKeyStorage {

    private final Connection connection;
    private final Logger logger;

    public SQLiteVirtualKeyStorage(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    @Override
    public void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS virtual_keys (
                player_uuid TEXT NOT NULL,
                crate_id TEXT NOT NULL,
                amount INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY (player_uuid, crate_id)
            )
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nella creazione tabella virtual_keys", e);
        }
    }

    @Override
    public void giveKeys(UUID playerUUID, String crateId, int amount) {
        if (amount <= 0) return;
        String sql = """
            INSERT INTO virtual_keys(player_uuid, crate_id, amount) VALUES (?, ?, ?)
            ON CONFLICT(player_uuid, crate_id) DO UPDATE SET amount = amount + excluded.amount
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, crateId);
            ps.setInt(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nell'aggiunta di chiavi virtuali", e);
        }
    }

    @Override
    public int getKeys(UUID playerUUID, String crateId) {
        String sql = "SELECT amount FROM virtual_keys WHERE player_uuid = ? AND crate_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, crateId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("amount");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nel recupero chiavi virtuali", e);
        }
        return 0;
    }

    @Override
    public void takeKeys(UUID playerUUID, String crateId, int amount) {
        if (amount <= 0) return;

        String sql = """
            UPDATE virtual_keys SET amount = amount - ?
            WHERE player_uuid = ? AND crate_id = ? AND amount >= ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setString(2, playerUUID.toString());
            ps.setString(3, crateId);
            ps.setInt(4, amount);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                logger.warning("Tentativo di rimuovere più chiavi di quelle disponibili per " + playerUUID + " crate " + crateId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nella rimozione chiavi virtuali", e);
        }
    }

    @Override
    public Map<String, Integer> getAllKeys(UUID playerUUID) {
        Map<String, Integer> keys = new HashMap<>();
        String sql = "SELECT crate_id, amount FROM virtual_keys WHERE player_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    keys.put(rs.getString("crate_id"), rs.getInt("amount"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nel recupero di tutte le chiavi virtuali", e);
        }
        return keys;
    }


    @Override
    public boolean hasKeys(UUID playerUUID, String crateId, int requiredAmount) {
        if (requiredAmount <= 0) return true;
        return getKeys(playerUUID, crateId) >= requiredAmount;
    }
}
