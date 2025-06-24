package com.monkey.ultimateCrates.bukkit.crates.particles.manager;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.particles.effect.EffectRemover;
import com.monkey.ultimateCrates.bukkit.crates.particles.effect.FixedEffectSpawner;
import com.monkey.ultimateCrates.bukkit.crates.particles.loader.ParticleEffectsLoader;
import com.monkey.ultimateCrates.bukkit.crates.particles.loader.ParticleStylesLoader;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.ParticleStyle;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticlesManager {

    private final UltimateCrates plugin;
    private final PlayerParticlesAPI api;

    private final Map<String, ParticleEffect> particleEffects = new HashMap<>();
    private final Map<String, ParticleStyle> particleStyles = new HashMap<>();

    private final FixedEffectSpawner spawner;
    private final EffectRemover remover;

    public ParticlesManager(UltimateCrates plugin) {
        this.plugin = plugin;

        this.api = initAPI();

        ParticleEffectsLoader.loadSupportedEffects().forEach(effect ->
                particleEffects.put(effect.getName().toLowerCase(), effect)
        );

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ParticleStyleManager styleManager = dev.esophose.playerparticles.PlayerParticles.getInstance().getManager(ParticleStyleManager.class);
            List<ParticleStyle> supportedStyles = ParticleStylesLoader.loadSupportedStyles(styleManager);

            for (ParticleStyle style : supportedStyles) {
                particleStyles.put(style.getInternalName().toLowerCase(), style);
                plugin.getLogger().info("Style trovato: " + style.getInternalName().toLowerCase());
            }

            plugin.getLogger().info("UltimateCrates: caricati " + particleEffects.size() + " effetti e " + particleStyles.size() + " stili.");
        }, 20L);


        this.spawner = new FixedEffectSpawner(plugin, api, particleEffects, particleStyles);
        this.remover = new EffectRemover(plugin, api);
    }

    private PlayerParticlesAPI initAPI() {
        if (plugin.getServer().getPluginManager().getPlugin("PlayerParticles") == null) {
            throw new IllegalStateException("PlayerParticles plugin is not installed or enabled!");
        }
        return PlayerParticlesAPI.getInstance();
    }

    public Integer spawnFixedEffectAt(Location location, String effectName, String styleName) {
        return spawner.spawnFixedEffectAt(location, effectName, styleName);
    }

    public void removeEffectAt(Location location) {
        remover.removeEffectAt(location);
    }
}
