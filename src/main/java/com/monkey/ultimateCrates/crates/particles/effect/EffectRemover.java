package com.monkey.ultimateCrates.crates.particles.effect;

import com.monkey.ultimateCrates.UltimateCrates;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import org.bukkit.Location;

import java.sql.SQLException;

public class EffectRemover {

    private final UltimateCrates plugin;
    private final PlayerParticlesAPI api;

    public EffectRemover(UltimateCrates plugin, PlayerParticlesAPI api) {
        this.plugin = plugin;
        this.api = api;
    }

    public void removeEffectAt(Location location) {
        try {
            Integer effectId = UltimateCrates.getInstance().getDatabaseCrates().getFixedEffectIdAt(location);
            if (effectId != null) {
                api.removeFixedEffect(plugin.getServer().getConsoleSender(), effectId);
                plugin.getLogger().info("Effetto particellare rimosso dalla posizione " + location);
                UltimateCrates.getInstance().getDatabaseCrates().removeFixedParticle(location);
            } else {
                plugin.getLogger().info("Nessun effetto particellare attivo da rimuovere in " + location);
            }
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore rimozione effetto particellare dal DB: " + ex.getMessage());
        }
    }
}
