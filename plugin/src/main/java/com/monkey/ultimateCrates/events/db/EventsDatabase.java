package com.monkey.ultimateCrates.events.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class EventsDatabase {

    private static EventsDatabase instance;
    private Connection connection;

    private static final String DB_URL = "jdbc:sqlite:plugins/UltimateCrates/events.db";

    private EventsDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
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
