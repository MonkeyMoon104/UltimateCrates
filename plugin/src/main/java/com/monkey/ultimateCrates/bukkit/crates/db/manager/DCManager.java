package com.monkey.ultimateCrates.bukkit.crates.db.manager;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DCManager {

    private final UltimateCrates plugin;
    private Connection connection;

    public DCManager(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) return;

        File storageFolder = new File(plugin.getDataFolder(), "storage");
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }

        File dbFile = new File(storageFolder, "crates.db");
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        createTablesIfNotExist();
    }


    private void createTablesIfNotExist() throws SQLException {
        String sqlPlacedCrates = """
            CREATE TABLE IF NOT EXISTS placed_crates (
                world TEXT NOT NULL,
                x INTEGER NOT NULL,
                y INTEGER NOT NULL,
                z INTEGER NOT NULL,
                crate_id TEXT NOT NULL,
                PRIMARY KEY (world, x, y, z)
            );
            """;

        String sqlFixedParticles = """
            CREATE TABLE IF NOT EXISTS fixed_particles (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                world TEXT NOT NULL,
                x INTEGER NOT NULL,
                y INTEGER NOT NULL,
                z INTEGER NOT NULL,
                particle_name TEXT NOT NULL,
                style TEXT NOT NULL,
                effect_id INTEGER NOT NULL,
                UNIQUE(world, x, y, z)
            );
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlPlacedCrates);
            stmt.execute(sqlFixedParticles);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
