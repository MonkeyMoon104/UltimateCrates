package com.monkey.ultimateCrates.bukkit.crates.util;

import com.monkey.ultimateCrates.bukkit.crates.model.CratePrize;

import java.util.List;
import java.util.Random;

public class CratePrizeSelector {

    private static final Random random = new Random();

    public static CratePrize selectPrize(List<CratePrize> prizes) {
        double totalChance = prizes.stream().mapToDouble(CratePrize::getChance).sum();
        double roll = random.nextDouble() * totalChance;

        double current = 0;
        for (CratePrize prize : prizes) {
            current += prize.getChance();
            if (roll <= current) {
                return prize;
            }
        }

        return null;
    }
}
