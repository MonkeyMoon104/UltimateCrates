package com.monkey.ultimateCrates.bukkit.proxy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;

public class ProxyMSG {

    private final Plugin plugin;
    private final FileConfiguration config;

    public ProxyMSG(Plugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "uc:notify");
    }

    public void sendEventNotifyToProxy(Player sender, String eventName, String duration, String serverName) {
        String data = sender.getName() + "|" + eventName + "|" + duration + "|" + serverName;
        sendPluginMessage(sender, data);
        plugin.getLogger().info("[UC] Event notify message sent to proxy: " + data);
    }

    private void sendPluginMessage(Player sender, String data) {
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(byteArray)) {
            out.write(data.getBytes(StandardCharsets.UTF_8));
            sender.sendPluginMessage(plugin, "uc:notify", byteArray.toByteArray());
        } catch (Exception e) {
            plugin.getLogger().severe("[UC] Error while sending plugin message to proxy: " + e.getMessage());
        }
    }
}
