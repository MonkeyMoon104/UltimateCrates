package com.monkey.ultimateCrates.bukkit.crates.db.repo;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.db.manager.DCManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PlacedCratesRepository {

    private final UltimateCrates plugin;
    private final DCManager connectionManager;

    public PlacedCratesRepository(UltimateCrates plugin, DCManager connectionManager) {
        this.plugin = plugin;
        this.connectionManager = connectionManager;
    }

    public void savePlacedCrate(Location loc, String crateId) throws SQLException {
        String sql = """
            INSERT OR REPLACE INTO placed_crates (world, x, y, z, crate_id)
            VALUES (?, ?, ?, ?, ?);
            """;

        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(sql)) {
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

        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(sql)) {
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

        try (Statement stmt = connectionManager.getConnection().createStatement();
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
}
