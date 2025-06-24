package com.monkey.ultimateCrates.bukkit.util;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AnimationUtils {

    public static void playCrateAnimations(UltimateCrates plugin, Player player, Crate crate) {
        var animationManager = plugin.getAnimationManager();
        Location location = player.getLocation();

        for (String animationName : crate.getAnimationTemplates()) {
            animationManager.getAnimation(animationName).ifPresentOrElse(
                    animation -> animation.play(player, location),
                    () -> plugin.getLogger().warning("Animazione non trovata: " + animationName + " nella crate: " + crate.getId())
            );
        }
    }

    public static void playEventAnimations(UltimateCrates plugin, Player player, String eventName) {
        if (eventName == null || eventName.isEmpty()) return;

        var crateEventsManager = plugin.getCrateEventsManager();
        var animationManager = plugin.getAnimationManager();
        Location location = player.getLocation();

        crateEventsManager.getPlayerAnimationForEvent(eventName).ifPresent(animationName -> {
            animationManager.getAnimation(animationName).ifPresentOrElse(
                    animation -> animation.play(player, location),
                    () -> plugin.getLogger().warning("Animazione evento non trovata: " + animationName + " per evento: " + eventName)
            );
        });
    }

    public static void playEventAnimationsOnChest(UltimateCrates plugin, Location location, String eventName) {
        if (eventName == null || eventName.isEmpty()) return;

        var crateEventsManager = plugin.getCrateEventsManager();
        var animationManager = plugin.getAnimationManager();

        crateEventsManager.getCratesAnimationForEvent(eventName).ifPresent(animationName -> {
            animationManager.getAnimation(animationName).ifPresentOrElse(
                    animation -> animation.play(null, location),
                    () -> plugin.getLogger().warning("Animazione evento non trovata: " + animationName + " per evento: " + eventName)
            );
        });
    }

}
