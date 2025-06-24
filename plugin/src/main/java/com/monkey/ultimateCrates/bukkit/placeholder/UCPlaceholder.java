package com.monkey.ultimateCrates.bukkit.placeholder;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.placeholder.helper.LPH;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class UCPlaceholder extends PlaceholderExpansion {

    private final UltimateCrates plugin;

    public UCPlaceholder(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "ultimatecrates";
    }

    @Override
    public String getAuthor() {
        return "MonkeyMoon104";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.startsWith("leaderboard_")) {
            return LPH.handle(plugin, identifier);
        }
        return null;
    }
}
