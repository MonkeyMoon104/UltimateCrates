package com.monkey.ultimateCrates.bukkit.config;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CratesConfigManager {

    private final UltimateCrates plugin;
    private FileConfiguration cratesConfig;
    private File cratesFile;

    public CratesConfigManager(UltimateCrates plugin) {
        this.plugin = plugin;
        this.cratesFile = new File(plugin.getDataFolder(), "crates.yml");
    }

    public void loadCratesConfig() {
        if (!cratesFile.exists()) {
            plugin.saveResource("crates.yml", false);
        }
        cratesConfig = YamlConfiguration.loadConfiguration(cratesFile);
    }

    public void reloadCratesConfig() {
        cratesConfig = YamlConfiguration.loadConfiguration(cratesFile);
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

    public ConfigurationSection getCratesSection() {
        return cratesConfig.getConfigurationSection("crates");
    }

}
