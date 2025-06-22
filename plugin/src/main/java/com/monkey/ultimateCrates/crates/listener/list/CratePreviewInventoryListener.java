package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.crates.listener.helper.manager.CratePreviewManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CratePreviewInventoryListener implements Listener {

    private final CratePreviewManager previewManager;

    public CratePreviewInventoryListener(CratePreviewManager previewManager) {
        this.previewManager = previewManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView() == null) return;
        if (event.getCurrentItem() == null) return;

        String title = event.getView().getTitle();
        if (title == null) return;

        if (previewManager.isPreviewTitle(title)) {
            event.setCancelled(true);
        }
    }
}
