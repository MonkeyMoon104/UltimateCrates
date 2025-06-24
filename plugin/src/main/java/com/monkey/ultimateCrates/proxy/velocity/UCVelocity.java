package com.monkey.ultimateCrates.proxy.velocity;

import com.google.inject.Inject;
import com.monkey.ultimateCrates.proxy.velocity.commands.InternalTeleportCommand;
import com.monkey.ultimateCrates.proxy.velocity.commands.UCVelocityReloadCommand;
import com.monkey.ultimateCrates.proxy.velocity.listener.EventMessageListener;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "ultimatecrates",
        name = "UltimateCrates",
        version = "1.1.8",
        authors = {"MonkeyMoon104"},
        description = "Proxy Crates"
)
public class UCVelocity {

    private final ProxyServer server;
    private final Logger logger;
    private ConfigurationNode config;
    private final Path dataFolder;
    private YamlConfigurationLoader loader;
    public static final LegacyChannelIdentifier CHANNEL = new LegacyChannelIdentifier("uc:notify");

    @Inject
    public UCVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory;
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        logger.info("Proxy ('Velocity') found!");
        loadConfig();

        server.getEventManager().register(this, new EventMessageListener(this));

        CommandManager cmdManager = server.getCommandManager();
        cmdManager.register(
                cmdManager.metaBuilder("ucpreload").build(),
                new UCVelocityReloadCommand(this)
        );
        cmdManager.register(
                cmdManager.metaBuilder("_u_c_t_").build(),
                new InternalTeleportCommand(this)
        );

        server.getChannelRegistrar().register(new LegacyChannelIdentifier("uc:notify"));
        logger.info("Channel plugin message successfully registered.");


        logger.info("UCProxy enabled.");
    }

    public void loadConfig() {
        try {
            Path configFile = dataFolder.resolve("configproxy.yml");
            if (Files.notExists(configFile)) {
                Files.createDirectories(dataFolder);
                try (InputStream in = getClass().getResourceAsStream("/configproxy.yml")) {
                    if (in != null) {
                        Files.copy(in, configFile);
                    } else {
                        logger.warn("configproxy.yml not found in jar! Please report this to the owner.");
                    }
                }
            }

            loader = YamlConfigurationLoader.builder()
                    .path(configFile)
                    .build();

            config = loader.load();
            logger.info("Configuration successfully loaded.");

        } catch (IOException e) {
            logger.error("Error loading configproxy.yml", e);
        }
    }

    public void reloadConfig() {
        try {
            config = loader.load();
            logger.info("Config successfully reloaded.");
        } catch (IOException e) {
            logger.error("Error reloading config", e);
        }
    }

    public ConfigurationNode getConfig() {
        return config;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }
}
