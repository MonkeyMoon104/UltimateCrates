package com.monkey.ultimateCrates.bukkit.config;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessagesManager {

    private final UltimateCrates plugin;
    private File messagesFile;
    private FileConfiguration messagesConfig;

    public MessagesManager(UltimateCrates plugin) {
        this.plugin = plugin;
        loadMessagesConfig();
    }

    private void loadMessagesConfig() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void reload() {
        loadMessagesConfig();
    }

    public String getMessage(String path) {
        if (!messagesConfig.contains(path)) return "§cMessaggio non trovato: " + path;
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString(path));
    }

    public String getMessage(String path, String placeholder, String replacement) {
        return getMessage(path).replace(placeholder, replacement);
    }

    public String getMessage(String key, Object... replacements) {
        String message = getMessage(key);
        if (message == null) return "";

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("%" + i + "%", String.valueOf(replacements[i]));
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
