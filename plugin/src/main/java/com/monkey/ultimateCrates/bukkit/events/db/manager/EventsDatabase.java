package com.monkey.ultimateCrates.bukkit.events.db.manager;

import com.monkey.ultimateCrates.bukkit.events.db.general.DatabaseConnection;
import com.monkey.ultimateCrates.bukkit.events.db.general.tables.TablesInitializer;

import java.sql.Connection;

public class EventsDatabase {

    private static EventsDatabase instance;
    private final Connection connection;

    private EventsDatabase() {
        this.connection = DatabaseConnection.createConnection();
        TablesInitializer.setup(connection);
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

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
