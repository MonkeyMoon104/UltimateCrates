package com.monkey.ultimateCrates.bukkit.events.db.general.tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TablesInitializer {

    public static void setup(Connection connection) {
        if (connection == null) return;

        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS keyhunt_active_chest (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    world TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    crate_id TEXT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS treasurehunt_active_chest (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    world TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    crate_id TEXT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS statshunt_active_chest (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    world TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    crate_id TEXT NOT NULL
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
