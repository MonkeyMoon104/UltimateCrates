package com.monkey.ultimateCrates.proxy.bungeecord.commands;

import com.monkey.ultimateCrates.proxy.bungeecord.UCBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class UCBungeeReloadCommand extends Command {

    private final UCBungee plugin;

    public UCBungeeReloadCommand(UCBungee plugin) {
        super("ucpreload", "ucproxy.reload");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("ucproxy.reload") ) {
            sender.sendMessage(new TextComponent(colorize(plugin.getConfig().getString("messages.no_permission", "&cYou not have permission"))));
            return;
        }

        plugin.reloadConfig();
        sender.sendMessage(new TextComponent(colorize(plugin.getConfig().getString("messages.reload_success", "UCProxy successfully reloaded."))));
    }

    private String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
