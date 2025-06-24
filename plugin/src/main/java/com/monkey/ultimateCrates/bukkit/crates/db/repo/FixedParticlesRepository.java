package com.monkey.ultimateCrates.bukkit.crates.db.repo;

import com.monkey.ultimateCrates.bukkit.crates.db.manager.DCManager;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FixedParticlesRepository {

    private final DCManager connectionManager;

    public FixedParticlesRepository(DCManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void saveFixedParticle(Location loc, String particleName, String style, int effectId) throws SQLException {
        String sql = """
            INSERT OR REPLACE INTO fixed_particles (world, x, y, z, particle_name, style, effect_id)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(sql)) {
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

        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());
            ps.executeUpdate();
        }
    }

    public Integer getFixedEffectIdAt(Location loc) throws SQLException {
        String sql = "SELECT effect_id FROM fixed_particles WHERE world = ? AND x = ? AND y = ? AND z = ?;";

        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());

            try (ResultSet rs = ps.executeQuery()) {
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

    public String getFixedEffectTypeAt(Location loc) throws SQLException {
        String sql = "SELECT particle_name FROM fixed_particles WHERE world = ? AND x = ? AND y = ? AND z = ?;";

        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("particle_name");
                }
                return null;
            }
        }
    }

    public String getFixedEffectStyleAt(Location loc) throws SQLException {
        String sql = "SELECT style FROM fixed_particles WHERE world = ? AND x = ? AND y = ? AND z = ?;";

        try (PreparedStatement ps = connectionManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, loc.getWorld().getName());
            ps.setInt(2, loc.getBlockX());
            ps.setInt(3, loc.getBlockY());
            ps.setInt(4, loc.getBlockZ());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("style");
                }
                return null;
            }
        }
    }
}
