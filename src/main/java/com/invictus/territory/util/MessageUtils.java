package com.invictus.territory.util;

import com.invictus.territory.model.Territory;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class MessageUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    /**
     * Convierte texto legacy con códigos de color (&) a Component moderno
     * Soporta colores hex con formato &#RRGGBB
     */
    public static Component colorize(String message) {
        String processed = translateHexColorCodes(message);
        return LegacyComponentSerializer.legacyAmpersand().deserialize(processed);
    }

    /**
     * Convierte texto legacy con códigos de color (§) a Component moderno
     * Soporta colores hex con formato &#RRGGBB
     */
    public static Component colorizeLegacy(String message) {
        String processed = translateHexColorCodes(message);
        return LegacyComponentSerializer.legacySection().deserialize(processed);
    }

    /**
     * Convierte códigos hex &#RRGGBB al formato que Adventure entiende
     * Formato: §x§R§R§G§G§B§B (con §, no &)
     */
    private static String translateHexColorCodes(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            // Convertir a formato legacy de hex que Minecraft entiende: §x§R§R§G§G§B§B
            String replacement = "§x"
                + "§" + hexCode.charAt(0)
                + "§" + hexCode.charAt(1)
                + "§" + hexCode.charAt(2)
                + "§" + hexCode.charAt(3)
                + "§" + hexCode.charAt(4)
                + "§" + hexCode.charAt(5);
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);

        return buffer.toString();
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