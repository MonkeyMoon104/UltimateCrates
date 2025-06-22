package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.validators.CrateBlockValidator;
import com.monkey.ultimateCrates.crates.listener.helper.general.CrateOpener;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class CrateOpenListener implements Listener {

    private final UltimateCrates plugin;
    private final CrateBlockValidator crateBlockValidator;
    private final CrateOpener crateOpener;

    public CrateOpenListener(UltimateCrates plugin) {
        this.plugin = plugin;
        this.crateBlockValidator = new CrateBlockValidator(plugin);
        this.crateOpener = new CrateOpener(plugin);
    }

    @EventHandler
    public void onCrateUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getPlayer().isSneaking()) return;

        if (!crateBlockValidator.isCrateBlock(event)) return;

        event.setCancelled(true);

        NBTBlock nbtBlock = new NBTBlock(event.getClickedBlock());
        String crateId = nbtBlock.getData().getString("crate_id");

        crateOpener.tryOpenCrate(event.getPlayer(), crateId);
    }
}
