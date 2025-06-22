package com.monkey.ultimateCrates.crates.listener.helper.validators;

import com.monkey.ultimateCrates.UltimateCrates;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrateBlockValidator {

    private final UltimateCrates plugin;

    public CrateBlockValidator(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public boolean isCrateBlock(PlayerInteractEvent event, boolean sendMessageIfFalse) {
        NBTBlock nbtBlock = new NBTBlock(event.getClickedBlock());
        boolean hasCrateId = nbtBlock.getData().hasTag("crate_id");

        if (!hasCrateId && sendMessageIfFalse) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNon è una crate!"));
        }

        return hasCrateId;
    }

}
