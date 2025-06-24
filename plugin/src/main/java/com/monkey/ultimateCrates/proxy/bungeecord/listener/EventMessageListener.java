package com.monkey.ultimateCrates.proxy.bungeecord.listener;

import com.monkey.ultimateCrates.proxy.bungeecord.UCBungee;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventMessageListener implements Listener {

    private final UCBungee plugin;
    private static final String CHANNEL = "uc:notify";

    public EventMessageListener(UCBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase(CHANNEL)) return;

        byte[] data = event.getData();
        String message = new String(data, StandardCharsets.UTF_8);

        String[] parts = message.split("\\|", 4);
        if (parts.length < 4) {
            plugin.getLogger().warning("Invalid event message received: " + message);
            return;
        }

        String senderName = parts[0];
        String eventName = parts[1];
        String eventEnd = parts[2];
        String serverName = parts[3];

        String title = plugin.getConfig().getString("event_proxy_notify.title", "&eNEW EVENT STARTED")
                .replace("%event%", eventName)
                .replace("%eventEnd%", eventEnd)
                .replace("%server%", serverName);

        AtomicInteger notified = new AtomicInteger();

        String teleportText = plugin.getConfig().getString("messages.server_textcomponent", "&a[Click to teleport to event]");

        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.hasPermission("ucproxy.event.notify")) {
                player.sendMessage(new TextComponent(colorize(title)));

                List<String> lines = plugin.getConfig().getStringList("event_proxy_notify.lines");
                for (String line : lines) {
                    line = line.replace("%event%", eventName)
                            .replace("%eventEnd%", eventEnd)
                            .replace("%server%", serverName);
                    player.sendMessage(new TextComponent(colorize(line)));
                }

                if (!serverName.equals("Unknown/Offline")) {
                    String teleportCommand = "/_u_c_t_ " + senderName + " " + serverName + " " + player.getName();

                    TextComponent clickable = new TextComponent(colorize(teleportText));
                    clickable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, teleportCommand));
                    clickable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(colorize(teleportText)).create()));

                    player.sendMessage(clickable);
                }

                notified.incrementAndGet();
            }
        }

        plugin.getLogger().info("Event broadcasted to " + notified.get() + " player(s).");
    }

    private String colorize(String message) {
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }
}
