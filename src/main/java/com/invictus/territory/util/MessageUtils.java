package com.invictus.territory.util;

import com.invictus.territory.model.Territory;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class MessageUtils {

    private static final LegacyComponentSerializer SERIALIZER =
        LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .build();

    /**
     * Convierte texto legacy con códigos de color (&) a Component moderno
     * Soporta colores hex con formato &#RRGGBB
     */
    public static Component colorize(String message) {
        return SERIALIZER.deserialize(message);
    }

    /**
     * Convierte texto legacy con códigos de color (§) a Component moderno
     * Convierte § a & internamente para unificar el procesamiento
     * Soporta colores hex con formato &#RRGGBB
     */
    public static Component colorizeLegacy(String message) {
        String converted = message.replace('§', '&');
        return SERIALIZER.deserialize(converted);
    }

    public static void notifyClanUpkeepPaid(UUID clanId, long amount, int territories, double multiplier) {
        Bukkit.getLogger().info(String.format(
                "[TerritoryCore] 💰 Clan %s pagó upkeep: $%d (%d territorios, %.1fx multiplier)",
                clanId, amount, territories, multiplier
        ));
    }

    public static void notifyClanUpkeepFailed(UUID clanId, long required, long current) {
        Bukkit.getLogger().warning(String.format(
                "[TerritoryCore] ⚠️ Clan %s NO pagó upkeep: necesitaba $%d pero tenía $%d",
                clanId, required, current
        ));
    }

    public static String getTerritorySummary(Territory territory) {
        return String.format(
                "🏰 %s | %s | Nivel %d | Upkeep: $%d/h",
                territory.getDisplayName(),
                territory.getSize(),
                territory.getLevel(),
                territory.getUpkeepBase()
        );
    }
}