package com.monkey.ultimateCrates.bukkit.util;

import com.monkey.ultimateCrates.bukkit.API.UltimateCratesApiEvent;
import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimatecrates.api.UltimateCratesAPI;

public class UCApi {

    public static void init() {
        if (UltimateCratesAPI.getProvider() == null) {
            UltimateCratesAPI.register(new UltimateCratesApiEvent(UltimateCrates.getInstance()), UltimateCrates.getInstance().getLogger());
        }
    }
}
