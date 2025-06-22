package com.monkey.ultimateCrates.config;

import com.monkey.ultimateCrates.UltimateCrates;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final UltimateCrates plugin;
    private FileConfiguration mainConfig;

    public ConfigManager(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void loadMainConfig() {
        plugin.saveDefaultConfig();
        mainConfig = plugin.getConfig();
    }

    public void reloadMainConfig() {
        plugin.reloadConfig();
        mainConfig = plugin.getConfig();
    }

    public FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public void saveMainConfig() {
        plugin.saveConfig();
    }
}