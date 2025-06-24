package com.monkey.ultimateCrates.proxy.velocity.commands;

import com.monkey.ultimateCrates.proxy.velocity.UCVelocity;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class UCVelocityReloadCommand implements SimpleCommand {

    private final UCVelocity plugin;

    public UCVelocityReloadCommand(UCVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();

        if (!source.hasPermission("ucproxy.reload")) {
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfig().node("messages", "no_permission").getString("&cYou not have sufficent permission.")));
            return;
        }

        plugin.reloadConfig();

        source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfig().node("messages", "reload_success").getString("&aUCProxy successfully reloaded.")));
    }
}
