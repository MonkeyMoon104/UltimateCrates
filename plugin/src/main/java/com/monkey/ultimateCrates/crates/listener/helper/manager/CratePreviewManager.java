package com.monkey.ultimateCrates.crates.listener.helper.manager;

import java.util.HashSet;
import java.util.Set;

public class CratePreviewManager {

    private final Set<String> previewInventoryTitles = new HashSet<>();

    public boolean isPreviewTitle(String title) {
        return previewInventoryTitles.contains(title);
    }

    public void addPreviewTitle(String title) {
        previewInventoryTitles.add(title);
    }
}
