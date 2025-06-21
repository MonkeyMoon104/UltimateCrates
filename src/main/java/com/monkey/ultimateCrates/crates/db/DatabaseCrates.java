package com.monkey.ultimateCrates.crates.db;

import com.monkey.ultimateCrates.UltimateCrates;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseCrates {

    private Connection connection;
    private final UltimateCrates plugin;

    public DatabaseCrates(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) return;

        File dbFile = new File(plugin.getDataFolder(), "crates.db");
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
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


    public void savePlacedCrate(Location loc, String crateId) throws SQLException {
        String sql = """
            INSERT OR REPLACE INTO placed_crates (world, x, y, z, crate_id)
            VALUES (?, ?, ?, ?, ?);
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());
            ps.setString(5, crateId);
            ps.executeUpdate();
        }
    }

    public void removePlacedCrate(Location loc) throws SQLException {
        String sql = """
            DELETE FROM placed_crates WHERE world = ? AND x = ? AND y = ? AND z = ?;
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());
            ps.executeUpdate();
        }
    }

    public Map<Location, String> loadAllPlacedCrates() throws SQLException {
        Map<Location, String> map = new HashMap<>();

        String sql = "SELECT world, x, y, z, crate_id FROM placed_crates;";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String world = rs.getString("world");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                String crateId = rs.getString("crate_id");

                Location loc = new Location(Bukkit.getWorld(world), x, y, z);
                if (loc.getWorld() != null) {
                    map.put(loc, crateId);
                } else {
                    plugin.getLogger().warning("World " + world + " not found for crate at " + x + "," + y + "," + z);
                }
            }
        }

        return map;
    }

    public void saveFixedParticle(Location loc, String particleName, String style, int effectId) throws SQLException {
        String sql = """
    INSERT OR REPLACE INTO fixed_particles (world, x, y, z, particle_name, style, effect_id)
    VALUES (?, ?, ?, ?, ?, ?, ?);
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());
            ps.setString(5, particleName);
            ps.setString(6, style);
            ps.setInt(7, effectId);
            ps.executeUpdate();
        }
    }

    public void removeFixedParticle(Location loc) throws SQLException {
        String sql = """
        DELETE FROM fixed_particles WHERE world = ? AND x = ? AND y = ? AND z = ?;
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());
            ps.executeUpdate();
        }
    }

    public Integer getFixedEffectIdAt(Location loc) throws SQLException {
        String sql = "SELECT effect_id FROM fixed_particles WHERE world = ? AND x = ? AND y = ? AND z = ?;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int effectId = rs.getInt("effect_id");
                    if (!rs.wasNull()) {
                        return effectId;
                    }
                }
                return null;
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
