package com.monkey.ultimateCrates.events.db;

import com.monkey.ultimateCrates.UltimateCrates;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class EventsDatabase {

    private static EventsDatabase instance;
    private Connection connection;

    private EventsDatabase() {
        try {

            File storageFolder = new File(UltimateCrates.getInstance().getDataFolder(), "storage");
            if (!storageFolder.exists()) {
                storageFolder.mkdirs();
            }

            File dbFile = new File(storageFolder, "events.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            setupTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized EventsDatabase getInstance() {
        if (instance == null) {
            instance = new EventsDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void setupTables() {
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

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
