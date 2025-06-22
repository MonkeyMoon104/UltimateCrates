package com.monkey.ultimateCrates.crates.manager;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.manager.loader.CrateLoader;
import com.monkey.ultimateCrates.crates.model.Crate;

import java.util.*;

public class CratesManager {

    private final UltimateCrates plugin;
    private final Map<String, Crate> crates = new HashMap<>();

    public CratesManager(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void loadCrates() {
        crates.clear();
        CrateLoader loader = new CrateLoader(plugin);
        Map<String, Crate> loaded = loader.load();
        crates.putAll(loaded);
        plugin.getLogger().info("Caricate " + crates.size() + " crate.");
    }

    public Optional<Crate> getCrate(String id) {
        return Optional.ofNullable(crates.get(id));
    }

    public Collection<Crate> getCrates() {
        return crates.values();
    }

    public List<String> getAllCrateIds() {
        return new ArrayList<>(crates.keySet());
    }
}
