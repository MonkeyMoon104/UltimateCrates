package com.monkey.ultimateCrates.crates.particles.effect;

import com.monkey.ultimateCrates.UltimateCrates;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.ParticleStyle;
import org.bukkit.Location;

import java.sql.SQLException;
import java.util.Map;

public class FixedEffectSpawner {

    private final UltimateCrates plugin;
    private final PlayerParticlesAPI api;
    private final Map<String, ParticleEffect> particleEffects;
    private final Map<String, ParticleStyle> particleStyles;

    public FixedEffectSpawner(UltimateCrates plugin, PlayerParticlesAPI api,
                              Map<String, ParticleEffect> particleEffects,
                              Map<String, ParticleStyle> particleStyles) {
        this.plugin = plugin;
        this.api = api;
        this.particleEffects = particleEffects;
        this.particleStyles = particleStyles;
    }

    public Integer spawnFixedEffectAt(Location location, String effectName, String styleName) {
        ParticleEffect effect = particleEffects.get(effectName.toLowerCase());
        ParticleStyle style = particleStyles.get(styleName.toLowerCase());

        if (effect == null || style == null) {
            plugin.getLogger().warning("Effetto o stile particellare non valido: " + effectName + ", " + styleName);
            return null;
        }

        Location center = location.clone().add(0.5, 1.0, 0.5);

        FixedParticleEffect created = api.createFixedParticleEffect(plugin.getServer().getConsoleSender(), center, effect, style);

        if (created == null) {
            plugin.getLogger().warning("Impossibile creare effetto particellare alla posizione " + location);
            return null;
        }

        try {
            UltimateCrates.getInstance().getDatabaseCrates()
                    .saveFixedParticle(location, effectName, styleName, created.getId());
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore nel salvataggio effetto particellare nel DB: " + ex.getMessage());
        }

        return created.getId();
    }
}
