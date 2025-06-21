package com.monkey.ultimateCrates.config;

import com.monkey.ultimateCrates.UltimateCrates;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final UltimateCrates plugin;
    private FileConfiguration mainConfig;
    private FileConfiguration cratesConfig;

    private File cratesFile;

    public ConfigManager(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        plugin.saveDefaultConfig();
        mainConfig = plugin.getConfig();

        cratesFile = new File(plugin.getDataFolder(), "crates.yml");
        if (!cratesFile.exists()) {
            plugin.saveResource("crates.yml", false);
        }
        cratesConfig = YamlConfiguration.loadConfiguration(cratesFile);
    }

    public FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public FileConfiguration getCratesConfig() {
        return cratesConfig;
    }

    public void saveCratesConfig() {
        try {
            cratesConfig.save(cratesFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Errore nel salvataggio di crates.yml");
            e.printStackTrace();
        }
    }
}

