package com.monkey.ultimateCrates.bukkit.crates.db.central;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.db.manager.DCManager;
import com.monkey.ultimateCrates.bukkit.crates.db.repo.FixedParticlesRepository;
import com.monkey.ultimateCrates.bukkit.crates.db.repo.PlacedCratesRepository;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.Map;

public class DatabaseCrates {

    private final DCManager connectionManager;
    private final PlacedCratesRepository placedCratesRepo;
    private final FixedParticlesRepository fixedParticlesRepo;

    public DatabaseCrates(UltimateCrates plugin) {
        this.connectionManager = new DCManager(plugin);
        this.placedCratesRepo = new PlacedCratesRepository(plugin, connectionManager);
        this.fixedParticlesRepo = new FixedParticlesRepository(connectionManager);
    }

    public void openConnection() throws SQLException {
        connectionManager.openConnection();
    }

    public void closeConnection() throws SQLException {
        connectionManager.closeConnection();
    }

    public void savePlacedCrate(Location loc, String crateId) throws SQLException {
        placedCratesRepo.savePlacedCrate(loc, crateId);
    }

    public void removePlacedCrate(Location loc) throws SQLException {
        placedCratesRepo.removePlacedCrate(loc);
    }

    public Map<Location, String> loadAllPlacedCrates() throws SQLException {
        return placedCratesRepo.loadAllPlacedCrates();
    }

    public void saveFixedParticle(Location loc, String particleName, String style, int effectId) throws SQLException {
        fixedParticlesRepo.saveFixedParticle(loc, particleName, style, effectId);
    }

    public void removeFixedParticle(Location loc) throws SQLException {
        fixedParticlesRepo.removeFixedParticle(loc);
    }

    public Integer getFixedEffectIdAt(Location loc) throws SQLException {
        return fixedParticlesRepo.getFixedEffectIdAt(loc);
    }

    public String getFixedEffectTypeAt(Location loc) throws SQLException {
        return fixedParticlesRepo.getFixedEffectTypeAt(loc);
    }

    public String getFixedEffectStyleAt(Location loc) throws SQLException {
        return fixedParticlesRepo.getFixedEffectStyleAt(loc);
    }
}
