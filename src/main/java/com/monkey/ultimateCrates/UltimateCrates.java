package com.monkey.ultimateCrates;

import com.monkey.ultimateCrates.command.CrateCommand;
import com.monkey.ultimateCrates.config.ConfigManager;
import com.monkey.ultimateCrates.crates.CratesManager;
import com.monkey.ultimateCrates.crates.animation.AnimationManager;
import com.monkey.ultimateCrates.crates.animation.list.*;
import com.monkey.ultimateCrates.crates.db.DatabaseCrates;
import com.monkey.ultimateCrates.crates.listener.CrateKeyInteractListener;
import com.monkey.ultimateCrates.crates.listener.CrateOpenListener;
import com.monkey.ultimateCrates.crates.listener.CratePlaceListener;
import com.monkey.ultimateCrates.crates.listener.CratePreviewListener;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.crates.model.ParticleEffectConfig;
import com.monkey.ultimateCrates.crates.particles.ParticlesManager;
import com.monkey.ultimateCrates.database.DatabaseManager;
import com.monkey.ultimateCrates.placeholder.UCPlaceholder;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class UltimateCrates extends JavaPlugin {

    private static UltimateCrates instance;

    private ConfigManager configManager;
    private CratesManager crateManager;
    private DatabaseManager databaseManager;
    private AnimationManager animationManager;
    private DatabaseCrates databaseCrates;
    private CratePlaceListener cratePlaceListener;
    private ParticlesManager particlesManager;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        configManager.loadConfigs();

        databaseManager = new DatabaseManager(this);
        databaseManager.setup();

        crateManager = new CratesManager(this);
        crateManager.loadCrates();
        databaseCrates = new DatabaseCrates(this);

        animationManager = new AnimationManager(this);
        particlesManager = new ParticlesManager(this);

        registerCommands();
        registerAnimations();
        registerListeners();
        registerExpansions();

        try {
            databaseCrates.openConnection();

            Map<Location, String> loadedCrates = databaseCrates.loadAllPlacedCrates();

            for (Map.Entry<Location, String> entry : loadedCrates.entrySet()) {
                Location loc = entry.getKey();
                String crateId = entry.getValue();

                spawnHologramAt(loc, crateId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Errore caricando crate dal DB!");
        }

        synchronizePlacedCratesEffects();

        getLogger().info("UltimateCrates enabled!");
    }

    @Override
    public void onDisable() {
        cratePlaceListener.getHolograms().values().forEach(list -> list.forEach(ArmorStand::remove));
        cratePlaceListener.getHolograms().clear();
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("UltimateCrates disabled.");
    }

    private void registerListeners() {
        cratePlaceListener = new CratePlaceListener();
        getServer().getPluginManager().registerEvents(cratePlaceListener, this);
        getServer().getPluginManager().registerEvents(new CratePreviewListener(), this);
        getServer().getPluginManager().registerEvents(new CrateKeyInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new CrateOpenListener(this), this);


    }

    private void registerCommands() {
        getCommand("crate").setExecutor(new CrateCommand());
    }

    private void registerAnimations() {
        animationManager.registerAnimation(new SparkleAnimation());
        animationManager.registerAnimation(new CircleAnimation());
        animationManager.registerAnimation(new SpiralAnimation());
        animationManager.registerAnimation(new FireworksAnimation());
        animationManager.registerAnimation(new RisingSmokeAnimation());
        animationManager.registerAnimation(new HeartBeatAnimation());
        animationManager.registerAnimation(new RainAnimation());
        animationManager.registerAnimation(new FlameRingAnimation());
        animationManager.registerAnimation(new PortalSwirlAnimation());
        animationManager.registerAnimation(new BubbleUpAnimation());

    }

    private void registerExpansions() {
        new UCPlaceholder(this).register();
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public ParticlesManager getParticlesManager() {
        return particlesManager;
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

    public void spawnHologramAt(Location loc, String crateId) {
        Optional<Crate> crateOpt = getCrateManager().getCrate(crateId);
        if (!crateOpt.isPresent()) {
            getLogger().warning("Crate con ID " + crateId + " non trovata per hologram.");
            return;
        }
        Crate crate = crateOpt.get();

        Location baseLoc = loc.clone().add(0.5, 1.5, 0.5);

        List<ArmorStand> stands = new ArrayList<>();

        for (int i = 0; i < crate.getHologramLines().size(); i++) {
            String line = crate.getHologramLines().get(i);
            Location lineLoc = baseLoc.clone().add(0, -0.25 * i, 0);

            ArmorStand stand = loc.getWorld().spawn(lineLoc, ArmorStand.class, armorStand -> {
                armorStand.setVisible(false);
                armorStand.setMarker(true);
                armorStand.setCustomNameVisible(true);
                armorStand.setCustomName(line.replace("&", "§"));
                armorStand.setGravity(false);
                armorStand.setSmall(true);
                armorStand.setInvulnerable(true);
                armorStand.setCollidable(false);
            });

            stands.add(stand);
        }

        cratePlaceListener.registerHologram(loc, stands);
    }

    public void reload() {
        cratePlaceListener.getHolograms().values().forEach(list -> list.forEach(ArmorStand::remove));
        cratePlaceListener.getHolograms().clear();

        configManager.reloadConfigs();

        crateManager.loadCrates();

        synchronizePlacedCratesEffects();

        animationManager.clear();
        registerAnimations();

        if (databaseManager != null) {
            databaseManager.close();
        }
        databaseManager.setup();

        try {
            databaseCrates.openConnection();
            Map<Location, String> loadedCrates = databaseCrates.loadAllPlacedCrates();
            for (Map.Entry<Location, String> entry : loadedCrates.entrySet()) {
                spawnHologramAt(entry.getKey(), entry.getValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Errore durante il reload del database crate!");
        }

        getLogger().info("UltimateCrates ricaricato con successo.");
    }

    public void synchronizePlacedCratesEffects() {
        try {
            Map<Location, String> placedCrates = getDatabaseCrates().loadAllPlacedCrates();

            for (Map.Entry<Location, String> entry : placedCrates.entrySet()) {
                Location location = entry.getKey();
                String crateId = entry.getValue();

                Optional<Crate> optionalCrate = getCrateManager().getCrate(crateId);
                if (optionalCrate.isEmpty()) continue;

                Crate crate = optionalCrate.get();
                ParticleEffectConfig config = crate.getParticleEffectConfig();

                if (config == null || !config.isEnabled()) {
                    getParticlesManager().removeEffectAt(location);
                    try {
                        getDatabaseCrates().removeFixedParticle(location);
                    } catch (SQLException ex) {
                        getLogger().severe("Errore nella rimozione dell'effetto disabilitato da DB: " + ex.getMessage());
                    }
                    continue;
                }

                Integer effectId = getDatabaseCrates().getFixedEffectIdAt(location);
                String savedEffectType = getDatabaseCrates().getFixedEffectTypeAt(location);
                String savedEffectStyle = getDatabaseCrates().getFixedEffectStyleAt(location);

                boolean needsUpdate = effectId == null
                        || !config.getType().equalsIgnoreCase(savedEffectType)
                        || !config.getEffect().equalsIgnoreCase(savedEffectStyle);

                if (needsUpdate) {
                    if (effectId != null) {
                        getParticlesManager().removeEffectAt(location);
                    }

                    try {
                        int spawnedId = getParticlesManager().spawnFixedEffectAt(location, config.getType(), config.getEffect());
                        getDatabaseCrates().saveFixedParticle(location, config.getType(), config.getEffect(), spawnedId);
                    } catch (Exception ex) {
                        getLogger().severe("Errore nella riapplicazione dell'effetto: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            getLogger().severe("Errore durante la sincronizzazione delle particelle: " + ex.getMessage());
        }
    }

}
