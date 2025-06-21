package com.monkey.ultimateCrates.database;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.database.impl.CrateOpenStorage;
import com.monkey.ultimateCrates.database.impl.SQLiteCrateOpenStorage;
import com.monkey.ultimateCrates.database.impl.SQLiteVirtualKeyStorage;
import com.monkey.ultimateCrates.database.impl.VirtualKeyStorage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseManager {

    private final UltimateCrates plugin;
    private Connection connection;
    private CrateOpenStorage crateOpenStorage;
    private VirtualKeyStorage virtualKeyStorage;

    public DatabaseManager(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        try {
            File dbFile = new File(plugin.getDataFolder(), "data.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);

            crateOpenStorage = new SQLiteCrateOpenStorage(connection);
            crateOpenStorage.createTable();

            virtualKeyStorage = new SQLiteVirtualKeyStorage(connection, plugin.getLogger());
            virtualKeyStorage.createTable();

            plugin.getLogger().info("Database SQLite caricato correttamente.");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Errore nella connessione al database SQLite: ", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public CrateOpenStorage getCrateOpenStorage() {
        return crateOpenStorage;
    }

    public VirtualKeyStorage getVirtualKeyStorage() {
        return virtualKeyStorage;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("Connessione SQLite chiusa.");
            } catch (SQLException e) {
                plugin.getLogger().warning("Errore chiusura database: " + e.getMessage());
            }
        }
    }
}
