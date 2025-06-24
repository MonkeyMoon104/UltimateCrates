package com.monkey.ultimateCrates.proxy.velocity.listener;

import com.monkey.ultimateCrates.proxy.velocity.UCVelocity;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventMessageListener {

    private final UCVelocity plugin;
    private final ProxyServer server;
    private final LegacyChannelIdentifier channel = new LegacyChannelIdentifier("uc:notify");

    public EventMessageListener(UCVelocity plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(channel)) return;

        byte[] data = event.getData();
        String message = new String(data, StandardCharsets.UTF_8);

        String[] parts = message.split("\\|", 4);
        if (parts.length < 4) {
            plugin.getLogger().warn("Invalid event message received: {}", message);
            return;
        }

        String senderName = parts[0];
        String eventName = parts[1];
        String eventEnd = parts[2];
        String serverName = parts[3];

        String title = plugin.getConfig().node("event_proxy_notify", "title").getString("&eNEW EVENT STARTED");
        title = replacePlaceholders(title, eventName, eventEnd, serverName);

        AtomicInteger notified = new AtomicInteger();

        String teleportText = plugin.getConfig().node("messages", "server_textcomponent")
                .getString("&a[Click to teleport to event]");

        for (Player player : server.getAllPlayers()) {
            if (player.hasPermission("ucproxy.event.notify")) {
                player.sendMessage(colorize(title));

                try {
                    List<String> lines = plugin.getConfig().node("event_proxy_notify", "lines").getList(String.class);
                    assert lines != null;
                    for (String line : lines) {
                        line = replacePlaceholders(line, eventName, eventEnd, serverName);
                        player.sendMessage(colorize(line));
                    }
                } catch (SerializationException e) {
                    plugin.getLogger().error("Error deserializing event lines", e);
                }

                if (!serverName.equals("Unknown/Offline")) {
                    String teleportCommand = "/_ucit_ " + senderName + " " + serverName + " " + player.getUsername();

                    Component clickable = colorize(teleportText)
                            .clickEvent(ClickEvent.runCommand(teleportCommand))
                            .hoverEvent(HoverEvent.showText(colorize(teleportText)));

                    player.sendMessage(clickable);
                }

                notified.incrementAndGet();
            }
        }

        plugin.getLogger().info("Event broadcasted to {} player(s).", notified.get());
    }

    private String replacePlaceholders(String input, String event, String eventEnd, String server) {
        return input.replace("%event%", event)
                .replace("%eventEnd%", eventEnd)
                .replace("%server%", server);
    }

    private Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
