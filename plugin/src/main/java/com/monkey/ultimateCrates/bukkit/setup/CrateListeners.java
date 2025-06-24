package com.monkey.ultimateCrates.bukkit.setup;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.listener.list.*;
import com.monkey.ultimateCrates.bukkit.crates.listener.helper.manager.CrateHologramManager;
import com.monkey.ultimateCrates.bukkit.crates.listener.helper.manager.CratePreviewManager;
import com.monkey.ultimateCrates.bukkit.events.listener.KeyHuntListener;
import com.monkey.ultimateCrates.bukkit.events.listener.StatsHuntListener;
import org.bukkit.plugin.PluginManager;

public class CrateListeners {

    public static void registerAll(UltimateCrates plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();

        CrateHologramManager hologramManager = plugin.getCrateHologramManager();
        CratePreviewManager previewManager = plugin.getCratePreviewManager();

        pm.registerEvents(new CratePlaceListener(plugin, hologramManager), plugin);
        pm.registerEvents(new CrateKeyInteractListener(plugin), plugin);
        pm.registerEvents(new CrateOpenListener(plugin), plugin);
        pm.registerEvents(new CrateBreakListener(hologramManager, plugin), plugin);
        pm.registerEvents(new CratePreviewInteractListener(previewManager, plugin), plugin);
        pm.registerEvents(new CratePreviewInventoryListener(previewManager), plugin);
        pm.registerEvents(new CrateRemoveListener(plugin, hologramManager), plugin);
        pm.registerEvents(new KeyHuntListener(), plugin);
        pm.registerEvents(new StatsHuntListener(), plugin);
        pm.registerEvents(new CrateProtectionListener(hologramManager), plugin);

    }
}
