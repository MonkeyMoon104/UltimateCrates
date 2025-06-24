package com.monkey.ultimateCrates.events.db.func;

import com.monkey.ultimateCrates.events.db.EventsDatabase;

import java.sql.*;

public class EventsDBFunctions {

    private final EventsDatabase database;

    public EventsDBFunctions() {
        database = EventsDatabase.getInstance();
    }

    public void saveKeyHuntChest(String world, int x, int y, int z, String crateId) {
        clearKeyHuntChest();
        String sql = "INSERT INTO keyhunt_active_chest (world, x, y, z, crate_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = database.getConnection().prepareStatement(sql)) {
            ps.setString(1, world);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            ps.setString(5, crateId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearKeyHuntChest() {
        String sql = "DELETE FROM keyhunt_active_chest";
        try (PreparedStatement ps = database.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTreasureHuntChest(String world, int x, int y, int z, String crateId) {
        clearTreasureHuntChest();
        String sql = "INSERT INTO treasurehunt_active_chest (world, x, y, z, crate_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = database.getConnection().prepareStatement(sql)) {
            ps.setString(1, world);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);
            ps.setString(5, crateId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearTreasureHuntChest() {
        String sql = "DELETE FROM treasurehunt_active_chest";
        try (PreparedStatement ps = database.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static class KeyHuntChest {
        private final String world;
        private final int x, y, z;
        private final String crateId;

        public KeyHuntChest(String world, int x, int y, int z, String crateId) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.crateId = crateId;
        }

        public String getWorld() { return world; }
        public int getX() { return x; }
        public int getY() { return y; }
        public int getZ() { return z; }
        public String getCrateId() { return crateId; }
    }

    public static class TreasureHuntChest {
        private final String world;
        private final int x, y, z;
        private final String crateId;

        public TreasureHuntChest(String world, int x, int y, int z, String crateId) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.crateId = crateId;
        }

        public String getWorld() { return world; }
        public int getX() { return x; }
        public int getY() { return y; }
        public int getZ() { return z; }
        public String getCrateId() { return crateId; }
    }
}
