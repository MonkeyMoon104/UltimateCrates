package com.monkey.ultimateCrates;

import com.monkey.ultimateCrates.command.CrateCommand;
import com.monkey.ultimateCrates.config.ConfigManager;
import com.monkey.ultimateCrates.config.CratesConfigManager;
import com.monkey.ultimateCrates.crates.listener.helper.manager.CrateHologramManager;
import com.monkey.ultimateCrates.crates.listener.helper.manager.CratePreviewManager;
import com.monkey.ultimateCrates.crates.manager.CratesManager;
import com.monkey.ultimateCrates.crates.animation.AnimationManager;
import com.monkey.ultimateCrates.crates.db.central.DatabaseCrates;
import com.monkey.ultimateCrates.crates.particles.manager.ParticlesManager;
import com.monkey.ultimateCrates.database.manager.DatabaseManager;
import com.monkey.ultimateCrates.placeholder.UCPlaceholder;
import com.monkey.ultimateCrates.setup.CrateAnimations;
import com.monkey.ultimateCrates.setup.CrateListeners;
import com.monkey.ultimateCrates.setup.PlacedCrateSync;
import com.monkey.ultimateCrates.setup.SpawnedHolograms;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

public final class UltimateCrates extends JavaPlugin {

    private static UltimateCrates instance;

    private ConfigManager configManager;
    private CratesManager crateManager;
    private DatabaseManager databaseManager;
    private AnimationManager animationManager;
    private DatabaseCrates databaseCrates;
    private ParticlesManager particlesManager;

    private CrateHologramManager crateHologramManager;
    private CratePreviewManager cratePreviewManager;
    private CratesConfigManager cratesConfigManager;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        configManager.loadMainConfig();

        cratesConfigManager = new CratesConfigManager(this);
        cratesConfigManager.loadCratesConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.setup();

        crateManager = new CratesManager(this);
        crateManager.loadCrates();

        databaseCrates = new DatabaseCrates(this);
        try {
            databaseCrates.openConnection();
        } catch (Exception e) {
            getLogger().severe("Errore durante l'apertura del database delle crates:");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        animationManager = new AnimationManager(this);
        particlesManager = new ParticlesManager(this);

        crateHologramManager = new CrateHologramManager();
        cratePreviewManager = new CratePreviewManager();

        getCommand("crate").setExecutor(new CrateCommand());

        CrateAnimations.registerAll(animationManager);
        CrateListeners.registerAll(this);

        new UCPlaceholder(this).register();

        SpawnedHolograms.loadAllFromDatabase(this, crateHologramManager);
        PlacedCrateSync.synchronize(this);

        getLogger().info("UltimateCrates enabled!");
    }

    @Override
    public void onDisable() {
        CrateHologramManager.getHolograms().values().forEach(list -> list.forEach(ArmorStand::remove));
        CrateHologramManager.getHolograms().clear();

        if (databaseManager != null) {
            databaseManager.close();
        }

        if (databaseCrates != null) {
            try {
                databaseCrates.closeConnection();
            } catch (Exception e) {
                getLogger().severe("Errore durante la chiusura del database delle crates:");
                e.printStackTrace();
            }
        }

        getLogger().info("UltimateCrates disabled.");
    }

    public void reload() {
        CrateHologramManager.getHolograms().values().forEach(list -> list.forEach(ArmorStand::remove));
        CrateHologramManager.getHolograms().clear();

        configManager.reloadMainConfig();
        cratesConfigManager.reloadCratesConfig();
        crateManager.loadCrates();

        PlacedCrateSync.synchronize(this);

        animationManager.clear();
        CrateAnimations.registerAll(animationManager);

        if (databaseManager != null) {
            databaseManager.close();
        }

        databaseManager.setup();

        SpawnedHolograms.loadAllFromDatabase(this, crateHologramManager);

        getLogger().info("UltimateCrates ricaricato con successo.");
    }

    public static UltimateCrates getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CratesManager getCrateManager() {
        return crateManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public DatabaseCrates getDatabaseCrates() {
        return databaseCrates;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public ParticlesManager getParticlesManager() {
        return particlesManager;
    }

    public CrateHologramManager getCrateHologramManager() {
        return crateHologramManager;
    }

    public CratePreviewManager getCratePreviewManager() {
        return cratePreviewManager;
    }

    public CratesConfigManager getCratesConfigManager() {
        return cratesConfigManager;
    }
}
