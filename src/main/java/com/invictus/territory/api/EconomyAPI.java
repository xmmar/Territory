package com.invictus.territory.api;

import java.util.UUID;

public class EconomyAPI {

    // TODO: Integrar con EconomyCore

    public long getClanBalance(UUID clanId) {
        // Placeholder
        return 0;
    }

    public boolean withdrawClan(UUID clanId, long amount) {
        // Placeholder
        return true;
    }

    public boolean depositClan(UUID clanId, long amount) {
        // Placeholder
        return true;
    }

    public String formatMoney(long amount) {
        if (amount >= 1_000_000) {
            return String.format("$%.2fM", amount / 1_000_000.0);
        } else if (amount >= 1_000) {
            return String.format("$%.2fK", amount / 1_000.0);
        }
        return "$" + amount;
    }
}