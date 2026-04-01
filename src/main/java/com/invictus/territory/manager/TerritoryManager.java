package com.invictus.territory.manager;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TerritoryManager {
    private final Map<String, Territory> territories = new HashMap<>();

    public TerritoryManager(TerritoryCore territoryCore) {
    }

    public void addTerritory(Territory territory) {
        territories.put(territory.getId(), territory);
        Bukkit.getLogger().info("[TerritoryCore] ✅ Territorio añadido: " + territory.getId());
    }

    public Territory getTerritory(String id) {
        return territories.get(id);
    }

    public List<Territory> getAllTerritories() {
        return new ArrayList<>(territories.values());
    }

    public List<Territory> getTerritoriesByOwner(UUID clanId) {
        List<Territory> result = new ArrayList<>();
        for (Territory territory : territories.values()) {
            if (territory.isOwned() && territory.getOwnerClanId().equals(clanId)) {
                result.add(territory);
            }
        }
        return result;
    }

    public void updateTerritory(Territory territory) {
        territories.put(territory.getId(), territory);
    }

    public void removeTerritory(String territoryId) {
        territories.remove(territoryId);
    }

    public int getTerritoryCount() {
        return territories.size();
    }

    public int getOwnedTerritoryCount(UUID clanId) {
        return (int) territories.values().stream()
                .filter(t -> t.isOwned() && t.getOwnerClanId().equals(clanId))
                .count();
    }

    public void teleportToTerritory(Player player, Territory territory) {
        World chronicleWorld = Bukkit.getWorld("CHRONICLE");

        if (chronicleWorld == null) {
            player.sendMessage(MessageUtils.colorize("&c❌ Error: El mundo CHRONICLE no existe"));
            return;
        }

        // Obtener el centro del territorio y sus límites
        Location center = territory.getBoundingBox().getCenter();
        int x = (int) center.getX();
        int y = (int) center.getY();
        int z = (int) center.getZ();

        // Obtener estadísticas del territorio
        String ownerStatus = territory.isOwned() ? "&c✖ Ocupado" : "&a✔ Disponible";
        String factionColor = getFactionColor(territory);

        // Enviar información del territorio
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorize(factionColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(MessageUtils.colorize(factionColor + "&l  " + getIcon(territory) + " " + territory.getDisplayName().toUpperCase()));
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorize("  &7📍 Coordenadas:"));
        player.sendMessage(MessageUtils.colorize("     &fX: &e" + x + " &7│ &fY: &e" + y + " &7│ &fZ: &e" + z));
        player.sendMessage(MessageUtils.colorize("     &7Mundo: &fCHRONICLE"));
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorize("  &7📊 Estadísticas:"));
        player.sendMessage(MessageUtils.colorize("     &7Nivel: &f" + territory.getLevel()));
        player.sendMessage(MessageUtils.colorize("     &7Tamaño: &f" + territory.getSize().name()));
        player.sendMessage(MessageUtils.colorize("     &7Categoría: &f" + territory.getCategory().getDisplayName()));
        player.sendMessage(MessageUtils.colorize("     &7Facción: " + factionColor + territory.getFaction().getDisplayName()));
        player.sendMessage(MessageUtils.colorize("     &7Estado: " + ownerStatus));
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorize("  &7💰 Economía:"));
        player.sendMessage(MessageUtils.colorize("     &7Producción: &a" + territory.getProduction() + "/día"));
        player.sendMessage(MessageUtils.colorize("     &7Mantenimiento: &c" + territory.getUpkeepBase() + "/día"));
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorize(factionColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage("");

        // Título con información principal
        Component title = MessageUtils.colorize(factionColor + "✦ " + territory.getDisplayName() + " ✦");
        Component subtitle = MessageUtils.colorize("&fX: &e" + x + " &7│ &fY: &e" + y + " &7│ &fZ: &e" + z + " &8│ " + ownerStatus);

        Title titleObj = Title.title(
            title,
            subtitle,
            Title.Times.times(
                Duration.ofMillis(500),
                Duration.ofMillis(5000),
                Duration.ofMillis(1000)
            )
        );

        player.showTitle(titleObj);

        // Mostrar información adicional en actionbar
        String actionBarText = factionColor + territory.getFaction().getDisplayName() +
                              " &8│ &7Nivel: &f" + territory.getLevel() +
                              " &8│ &7Producción: &a" + territory.getProduction() +
                              " &8│ &7Upkeep: &c" + territory.getUpkeepBase();
        player.sendActionBar(MessageUtils.colorize(actionBarText));
    }

    private String getFactionColor(Territory territory) {
        return switch (territory.getFaction()) {
            case GREECE -> "&3";
            case EGYPT -> "&6";
            case NEUTRAL -> "&5";
        };
    }

    private String getIcon(Territory territory) {
        return switch (territory.getFaction()) {
            case GREECE -> "⚔";
            case EGYPT -> "☥";
            case NEUTRAL -> "⚡";
        };
    }
}