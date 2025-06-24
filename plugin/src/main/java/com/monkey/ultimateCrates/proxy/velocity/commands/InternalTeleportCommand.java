package com.monkey.ultimateCrates.proxy.velocity.commands;

import com.monkey.ultimateCrates.proxy.velocity.UCVelocity;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Optional;

public class InternalTeleportCommand implements SimpleCommand {

    private final UCVelocity plugin;

    public InternalTeleportCommand(UCVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 3) {
            return;
        }

        String sender = args[0];
        String serverName = args[1];
        String target = args[2];

        if (!player.getUsername().equalsIgnoreCase(target)) {
            return;
        }

        Optional<RegisteredServer> optionalServer = plugin.getServer().getServer(serverName);
        if (optionalServer.isEmpty()) {
            player.sendMessage(colorize(plugin.getConfig().node("messages", "server_not_found").getString("&cServer not found.")));
            return;
        }

        RegisteredServer server = optionalServer.get();

        Optional<RegisteredServer> currentServer = player.getCurrentServer().map(ServerConnection::getServer);
        if (currentServer.isEmpty() || !currentServer.get().getServerInfo().getName().equalsIgnoreCase(serverName)) {
            player.createConnectionRequest(server).connect().exceptionally(e -> {
                player.sendMessage(colorize(plugin.getConfig().node("messages", "connect_failed").getString("&cFailed to connect to server.")));
                plugin.getLogger().error("Failed to connect player to server {}", serverName, e);
                return null;
            });
        }
    }

    private Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
