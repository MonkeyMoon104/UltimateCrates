package com.monkey.ultimateCrates.proxy.bungeecord.commands;

import com.monkey.ultimateCrates.proxy.bungeecord.UCBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class InternalTeleportCommand extends Command {

    private final UCBungee plugin;

    public InternalTeleportCommand(UCBungee plugin) {
        super("_u_c_t_", null, "_uc_t_");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;

        if (args.length < 3) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String senderName = args[0];
        String serverName = args[1];
        String targetName = args[2];

        if (!player.getName().equalsIgnoreCase(targetName)) return;

        ServerInfo server = plugin.getProxy().getServerInfo(serverName);
        if (server == null) {
            player.sendMessage(new TextComponent(colorize(plugin.getConfig().getString("messages.server_not_found", "&cServer not found."))));
            return;
        }

        if (player.getServer() == null || !player.getServer().getInfo().getName().equalsIgnoreCase(serverName)) {
            player.connect(server);
        }
    }

    private String colorize(String message) {
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }
}
