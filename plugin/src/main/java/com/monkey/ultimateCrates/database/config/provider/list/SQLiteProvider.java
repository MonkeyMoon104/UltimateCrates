package com.monkey.ultimateCrates.database.config.provider.list;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.database.config.provider.interf.DatabaseProvider;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLiteProvider implements DatabaseProvider {

    private final UltimateCrates plugin;

    public SQLiteProvider(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public Connection connect() {
        try {
            File storageFolder = new File(plugin.getDataFolder(), "storage");
            if (!storageFolder.exists()) {
                storageFolder.mkdirs();
            }

            File dbFile = new File(storageFolder, "data.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            Connection connection = DriverManager.getConnection(url);
            plugin.getLogger().info("Connessione SQLite stabilita.");
            return connection;
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Errore nella connessione SQLite: ", e);
            return null;
        }
    }
}
