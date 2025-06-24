package com.monkey.ultimateCrates.proxy.bungeecord;

import com.monkey.ultimateCrates.proxy.bungeecord.commands.InternalTeleportCommand;
import com.monkey.ultimateCrates.proxy.bungeecord.commands.UCBungeeReloadCommand;
import com.monkey.ultimateCrates.proxy.bungeecord.listener.EventMessageListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class UCBungee extends Plugin {

    private Configuration config;

    @Override
    public void onEnable() {
        getLogger().info("Proxy ('Bungeecord') found!");
        saveDefaultConfig();

        getProxy().registerChannel("uc:notify");

        getProxy().getPluginManager().registerListener(this, new EventMessageListener(this));
        getProxy().getPluginManager().registerCommand(this, new UCBungeeReloadCommand(this));
        getProxy().getPluginManager().registerCommand(this, new InternalTeleportCommand(this));

        getLogger().info("UCProxy enabled.");
    }

    public void saveDefaultConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "configproxy.yml");

        if (!configFile.exists()) {
            try (InputStream in = getResourceAsStream("configproxy.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Could not save default configproxy.yml", e);
            }
        }

        reloadConfig();
    }

    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "configproxy.yml"));
            getLogger().info("Config reloaded.");
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not load configproxy.yml", e);
        }
    }

    public Configuration getConfig() {
        return config;
    }
}
