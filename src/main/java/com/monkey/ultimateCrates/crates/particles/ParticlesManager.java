package com.monkey.ultimateCrates.crates.particles;

import com.monkey.ultimateCrates.UltimateCrates;
import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.styles.ParticleStyle.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.*;

public class ParticlesManager {

    private final UltimateCrates plugin;
    private final PlayerParticlesAPI api;

    private final Map<String, ParticleEffect> particleEffects = new HashMap<>();
    private final Map<String, ParticleStyle> particleStyles = new HashMap<>();
    private final Map<Location, FixedParticleEffect> activeEffects = new HashMap<>();

    private static final List<String> SUPPORTED_EFFECTS = Arrays.asList(
            "angry_villager", "ash", "block", "block_crumble", "block_marker", "bubble", "bubble_column_up",
            "bubble_pop", "campfire_cosy_smoke", "campfire_signal_smoke", "cherry_leaves", "cloud", "composter",
            "crimson_spore", "crit", "current_down", "damage_indicator", "dolphin", "dragon_breath",
            "dripping_dripstone_lava", "dripping_dripstone_water", "dripping_honey", "dripping_lava",
            "dripping_obsidian_tear", "dripping_water", "dust", "dust_color_transition", "dust_pillar",
            "dust_plume", "egg_crack", "electric_spark", "enchant", "enchanted_hit", "end_rod", "entity_effect",
            "explosion", "explosion_emitter", "falling_dripstone_lava", "falling_dripstone_water",
            "falling_dust", "falling_honey", "falling_lava", "falling_nectar", "falling_obsidian_tear",
            "falling_spore_blossom", "falling_water", "firework", "fishing", "flame", "glow", "glow_squid_ink",
            "gust", "gust_emitter_large", "gust_emitter_small", "happy_villager", "heart", "infested",
            "instant_effect", "item", "item_cobweb", "item_slime", "item_snowball", "landing_honey",
            "landing_lava", "landing_obsidian_tear", "large_smoke", "lava", "mycelium", "nautilus", "note",
            "ominous_spawning", "pale_oak_leaves", "poof", "portal", "raid_omen", "rain", "reverse_portal",
            "scrape", "sculk_charge", "sculk_charge_pop", "sculk_soul", "shriek", "small_flame", "small_gust",
            "smoke", "sneeze", "snowflake", "sonic_boom", "soul", "soul_fire_flame", "spell", "spit", "splash",
            "spore_blossom_air", "squid_ink", "sweep_attack", "totem_of_undying", "trail", "trial_omen",
            "trial_spawner_detection", "trial_spawner_detection_ominous", "underwater", "vault_connection",
            "warped_spore", "wax_off", "wax_on", "white_ash", "witch"
    );

    private static final List<String> SUPPORTED_STYLES = Arrays.asList(
            "arrows", "batman", "beam", "blockbreak", "blockplace", "celebration", "chains", "companion",
            "cube", "death", "feet", "fishing", "halo", "hurt", "icosphere", "invocation", "move", "normal",
            "orbit", "outline", "overhead", "point", "popper", "pulse", "quadhelix", "rings", "sphere", "spin",
            "spiral", "swords", "teleport", "thick", "trail", "twins", "vortex", "whirl", "whirlwind", "wings"
    );

    public ParticlesManager(UltimateCrates plugin) {
        this.plugin = plugin;
        Plugin playerParticlesPlugin = plugin.getServer().getPluginManager().getPlugin("PlayerParticles");
        if (playerParticlesPlugin == null || !playerParticlesPlugin.isEnabled()) {
            throw new IllegalStateException("PlayerParticles plugin is not installed or enabled!");
        }

        this.api = PlayerParticlesAPI.getInstance();

        for (String name : SUPPORTED_EFFECTS) {
            ParticleEffect effect = ParticleEffect.fromName(name);
            if (effect != null) {
                particleEffects.put(name.toLowerCase(), effect);
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

        ParticleStyleManager styleManager = PlayerParticles.getInstance().getManager(ParticleStyleManager.class);

        Collection<ParticleStyle> availableStyles = styleManager.getStyles();

        for (ParticleStyle style : availableStyles) {
            String styleName = style.getInternalName().toLowerCase();

            if (SUPPORTED_STYLES.contains(styleName)) {
                particleStyles.put(styleName, style);
                plugin.getLogger().info("Style trovato: " + styleName);
            }
        }
            plugin.getLogger().info("UltimateCrates: caricati " + particleEffects.size() + " effetti e " + particleStyles.size() + " stili.");
        }, 40L);
    }

    public Integer spawnFixedEffectAt(Location location, String effectName, String styleName) {
        ParticleEffect effect = this.particleEffects.get(effectName.toLowerCase());
        ParticleStyle style = this.particleStyles.get(styleName.toLowerCase());

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

        activeEffects.put(location, created);

        try {
            UltimateCrates.getInstance().getDatabaseCrates()
                    .saveFixedParticle(location, effectName, styleName, created.getId());
        } catch (SQLException ex) {
            plugin.getLogger().severe("Errore nel salvataggio effetto particellare nel DB: " + ex.getMessage());
        }

        return created.getId();
    }

    public void removeEffectAt(Location location) {
        try {
            Integer effectId = UltimateCrates.getInstance().getDatabaseCrates().getFixedEffectIdAt(location);
            if (effectId != null) {
                api.removeFixedEffect(plugin.getServer().getConsoleSender(), effectId);
                UltimateCrates.getInstance().getLogger().info("Effetto particellare rimosso dalla posizione " + location);

                UltimateCrates.getInstance().getDatabaseCrates().removeFixedParticle(location);
            } else {
                UltimateCrates.getInstance().getLogger().info("Nessun effetto particellare attivo da rimuovere in " + location);
            }
        } catch (SQLException ex) {
            UltimateCrates.getInstance().getLogger().severe("Errore rimozione effetto particellare dal DB: " + ex.getMessage());
        }
    }
}
