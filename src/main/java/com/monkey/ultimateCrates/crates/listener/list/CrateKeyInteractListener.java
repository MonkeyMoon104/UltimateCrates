package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.validators.CrateBlockValidator;
import com.monkey.ultimateCrates.crates.listener.helper.validators.CrateKeyValidator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CrateKeyInteractListener implements Listener {

    private final UltimateCrates plugin;
    private final CrateKeyValidator crateKeyValidator;
    private final CrateBlockValidator crateBlockValidator;

    public CrateKeyInteractListener(UltimateCrates plugin) {
        this.plugin = plugin;
        this.crateKeyValidator = new CrateKeyValidator();
        this.crateBlockValidator = new CrateBlockValidator(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return;

        if (!crateKeyValidator.isCrateKey(event.getItem())) return;

        if (action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            if (!crateBlockValidator.isCrateBlock(event, true)) {
                event.setCancelled(true);
                event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
            }
        }

    }
}
