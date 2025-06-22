package com.monkey.ultimateCrates.crates.particles.loader;

import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.styles.ParticleStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ParticleStylesLoader {

    private static final List<String> SUPPORTED_STYLES = Arrays.asList(
            "arrows", "batman", "beam", "blockbreak", "blockplace", "celebration", "chains", "companion",
            "cube", "death", "feet", "fishing", "halo", "hurt", "icosphere", "invocation", "move", "normal",
            "orbit", "outline", "overhead", "point", "popper", "pulse", "quadhelix", "rings", "sphere", "spin",
            "spiral", "swords", "teleport", "thick", "trail", "twins", "vortex", "whirl", "whirlwind", "wings"
    );

    public static List<ParticleStyle> loadSupportedStyles(ParticleStyleManager styleManager) {
        Collection<ParticleStyle> availableStyles = styleManager.getStyles();
        List<ParticleStyle> supported = new ArrayList<>();
        for (ParticleStyle style : availableStyles) {
            if (SUPPORTED_STYLES.contains(style.getInternalName().toLowerCase())) {
                supported.add(style);
            }
        }
        return supported;
    }
}
