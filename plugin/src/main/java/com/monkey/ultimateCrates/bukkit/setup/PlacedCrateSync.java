package com.monkey.ultimateCrates.bukkit.setup;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import com.monkey.ultimateCrates.bukkit.crates.model.PCE;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class PlacedCrateSync {

    public static void synchronize(UltimateCrates plugin) {
        try {
            Map<Location, String> placedCrates = plugin.getDatabaseCrates().loadAllPlacedCrates();

            for (Map.Entry<Location, String> entry : placedCrates.entrySet()) {
                Location location = entry.getKey();
                String crateId = entry.getValue();

                Optional<Crate> optionalCrate = plugin.getCrateManager().getCrate(crateId);
                if (optionalCrate.isEmpty()) continue;

                Crate crate = optionalCrate.get();
                PCE config = crate.getParticleEffectConfig();

                if (config == null || !config.isEnabled()) {
                    plugin.getParticlesManager().removeEffectAt(location);
                    try {
                        plugin.getDatabaseCrates().removeFixedParticle(location);
                    } catch (SQLException ex) {
                        plugin.getLogger().severe("Errore nella rimozione dell'effetto disabilitato da DB: " + ex.getMessage());
                    }
                    continue;
                }

                Integer effectId = plugin.getDatabaseCrates().getFixedEffectIdAt(location);
                String savedEffectType = plugin.getDatabaseCrates().getFixedEffectTypeAt(location);
                String savedEffectStyle = plugin.getDatabaseCrates().getFixedEffectStyleAt(location);

                boolean needsUpdate = effectId == null
                        || !config.getType().equalsIgnoreCase(savedEffectType)
                        || !config.getEffect().equalsIgnoreCase(savedEffectStyle);

                if (needsUpdate) {
                    if (effectId != null) {
                        plugin.getParticlesManager().removeEffectAt(location);
                    }

                    try {
                        int spawnedId = plugin.getParticlesManager().spawnFixedEffectAt(location, config.getType(), config.getEffect());
                        plugin.getDatabaseCrates().saveFixedParticle(location, config.getType(), config.getEffect(), spawnedId);
                    } catch (Exception ex) {
                        plugin.getLogger().severe("Errore nella riapplicazione dell'effetto: " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore durante la sincronizzazione delle particelle: " + ex.getMessage());
        }
    }
}
