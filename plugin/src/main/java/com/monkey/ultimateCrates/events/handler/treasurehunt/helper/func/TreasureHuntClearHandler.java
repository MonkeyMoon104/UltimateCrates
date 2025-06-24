package com.monkey.ultimateCrates.events.handler.treasurehunt.helper.func;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.events.handler.treasurehunt.helper.holo.TreasureHologramHandler;
import com.monkey.ultimateCrates.events.handler.treasurehunt.manager.TreasureHuntExecutor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import java.sql.SQLException;

public class TreasureHuntClearHandler {

    public static void execute() {
        var location = TreasureHuntExecutor.getCurrentTreasureLocation();
        if (location != null) {
            Block block = location.getBlock();
            if (block.getType() == Material.CHEST) {
                block.setType(Material.AIR);
            }

            try {
                UltimateCrates.getInstance().getDatabaseCrates().removePlacedCrate(location);
            } catch (SQLException e) {
                UltimateCrates.getInstance().getLogger().severe("Errore durante la rimozione della crate nel DB (clear Treasure): " + e.getMessage());
            }

            for (ArmorStand stand : TreasureHologramHandler.getSpawnedHolograms()) {
                stand.remove();
            }
            TreasureHologramHandler.clear();

            UltimateCrates.getInstance().getCrateHologramManager().removeHologram(location);
            UltimateCrates.getInstance().getParticlesManager().removeEffectAt(location);
            UltimateCrates.getInstance().getEventsDatabaseFunctions().clearTreasureHuntChest();
        }

        TreasureHuntExecutor.resetState();
    }
}
