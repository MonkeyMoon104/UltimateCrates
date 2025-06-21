package com.monkey.ultimateCrates.database.impl;

import com.monkey.ultimateCrates.database.impl.CrateOpenStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLiteCrateOpenStorage implements CrateOpenStorage {

    private final Connection connection;

    public SQLiteCrateOpenStorage(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() {
        try (PreparedStatement ps = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS crate_opens (" +
                        "player_uuid TEXT NOT NULL, " +
                        "crate_id TEXT NOT NULL, " +
                        "open_count INTEGER NOT NULL DEFAULT 0, " +
                        "PRIMARY KEY (player_uuid, crate_id))")) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOpen(UUID playerUUID, String crateId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO crate_opens (player_uuid, crate_id, open_count) " +
                        "VALUES (?, ?, 1) " +
                        "ON CONFLICT(player_uuid, crate_id) DO UPDATE SET open_count = open_count + 1")) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, crateId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getOpenCount(UUID playerUUID, String crateId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT open_count FROM crate_opens WHERE player_uuid = ? AND crate_id = ?")) {
            ps.setString(1, playerUUID.toString());
            ps.setString(2, crateId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("open_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
