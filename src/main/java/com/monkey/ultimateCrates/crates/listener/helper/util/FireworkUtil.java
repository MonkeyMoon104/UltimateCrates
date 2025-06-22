package com.monkey.ultimateCrates.crates.listener.helper.util;

import com.monkey.ultimateCrates.UltimateCrates;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireworkUtil {

    public static void startRewardFirework(Player player, UltimateCrates plugin) {
        startRewardFirework(player, plugin, 1);
    }

    public static void startRewardFirework(Player player, UltimateCrates plugin, int repeat) {
        for (int i = 0; i < repeat; i++) {
            int delay = i * 10;
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location location = player.getLocation().add(0, 1, 0);
                    Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK_ROCKET);
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.addEffect(getCircularFireworkEffectHype());
                    meta.setPower(1);
                    firework.setFireworkMeta(meta);
                }
            }.runTaskLater(plugin, delay);
        }
    }

    private static FireworkEffect getCircularFireworkEffectHype() {
        Random random = new Random();

        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            colors.add(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        List<Color> fades = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            fades.add(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        return FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(colors)
                .withFade(fades)
                .build();
    }
}