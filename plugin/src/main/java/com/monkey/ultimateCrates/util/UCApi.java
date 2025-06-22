package com.monkey.ultimateCrates.util;

import com.monkey.ultimateCrates.API.UltimateCratesApiEvent;
import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimatecrates.api.UltimateCratesAPI;

public class UCApi {

    public static void init() {
        if (UltimateCratesAPI.getProvider() == null) {
            UltimateCratesAPI.register(new UltimateCratesApiEvent(UltimateCrates.getInstance()), UltimateCrates.getInstance().getLogger());
        }
    }
}
