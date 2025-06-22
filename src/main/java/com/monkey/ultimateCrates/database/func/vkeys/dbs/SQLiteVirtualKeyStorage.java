package com.monkey.ultimateCrates.database.func.vkeys.dbs;

import com.monkey.ultimateCrates.database.func.vkeys.interf.VirtualKeyStorage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
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
                player_name TEXT NOT NULL,
                crate_id TEXT NOT NULL,
                amount INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY (player_name, crate_id)
            )
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nella creazione tabella virtual_keys", e);
        }
    }

    @Override
    public void giveKeys(String playerName, String crateId, int amount) {
        if (amount <= 0) return;
        String sql = """
            INSERT INTO virtual_keys(player_name, crate_id, amount) VALUES (?, ?, ?)
            ON CONFLICT(player_name, crate_id) DO UPDATE SET amount = amount + excluded.amount
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerName);
            ps.setString(2, crateId);
            ps.setInt(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nell'aggiunta di chiavi virtuali", e);
        }
    }

    @Override
    public int getKeys(String playerName, String crateId) {
        String sql = "SELECT amount FROM virtual_keys WHERE player_name = ? AND crate_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerName);
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
    public void takeKeys(String playerName, String crateId, int amount) {
        if (amount <= 0) return;

        String sql = """
            UPDATE virtual_keys SET amount = amount - ?
            WHERE player_name = ? AND crate_id = ? AND amount >= ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setString(2, playerName);
            ps.setString(3, crateId);
            ps.setInt(4, amount);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                logger.warning("Tentativo di rimuovere più chiavi di quelle disponibili per " + playerName + " crate " + crateId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nella rimozione chiavi virtuali", e);
        }
    }

    @Override
    public Map<String, Integer> getAllKeys(String playerName) {
        Map<String, Integer> keys = new HashMap<>();
        String sql = "SELECT crate_id, amount FROM virtual_keys WHERE player_name = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, playerName);
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
    public void resetKeys(String playerName) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM virtual_keys WHERE player_name = ?"
        )) {
            stmt.setString(1, playerName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.warning("Errore resettando chiavi per " + playerName + ": " + e.getMessage());
        }
    }

    @Override
    public void resetAllKeys() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM virtual_keys");
        } catch (SQLException e) {
            logger.warning("Errore resettando tutte le chiavi virtuali: " + e.getMessage());
        }
    }
}
