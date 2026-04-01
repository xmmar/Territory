package com.invictus.territory.listener;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.data.TerritoryData;
import com.invictus.territory.model.Territory;
import com.invictus.territory.model.BoundingBox;
import com.invictus.territory.manager.TerritoryMusicManager;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TerritoryStatusListener implements Listener {

    private final TerritoryCore plugin;
    private final Map<UUID, String> playerTerritories = new HashMap<>();
    private final Map<UUID, Long> lastMusicTime = new HashMap<>();
    private static final long MUSIC_COOLDOWN = 5000;
    private static final int MOVE_CHECK_DISTANCE = 5;
    private final Map<UUID, Location> lastCheckLocation = new HashMap<>();

    public TerritoryStatusListener(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        Location loc = player.getLocation();

        Location lastLoc = lastCheckLocation.get(playerId);
        if (lastLoc != null && loc.distance(lastLoc) < MOVE_CHECK_DISTANCE) {
            return;
        }
        lastCheckLocation.put(playerId, loc.clone());

        Territory currentTerritory = getTerritoryAt(loc);
        String currentTerritoryId = currentTerritory != null ? currentTerritory.getId() : null;
        String previousTerritoryId = playerTerritories.get(playerId);

        if (currentTerritoryId != null && !currentTerritoryId.equals(previousTerritoryId)) {
            playerTerritories.put(playerId, currentTerritoryId);

            long currentTime = System.currentTimeMillis();
            long lastTime = lastMusicTime.getOrDefault(playerId, 0L);

            if (currentTime - lastTime > MUSIC_COOLDOWN) {
                lastMusicTime.put(playerId, currentTime);
                sendTerritoryTitle(player, currentTerritory);
                TerritoryMusicManager.playTerritoryMusic(player, currentTerritory);
            }
        } else if (currentTerritoryId == null && previousTerritoryId != null) {
            playerTerritories.remove(playerId);
            lastMusicTime.remove(playerId);
        }
    }

    private void sendTerritoryTitle(Player player, Territory territory) {
        TerritoryData.TerritoryInfo info = getTerritoryInfo(territory.getId());

        if (info == null) {
            // Usar información del YAML si no está en TerritoryData
            sendEnhancedTerritoryMessage(player, territory);
            return;
        }

        // Título principal con estilo
        String titleText = info.primaryColor + "§l✦ " + territory.getDisplayName().toUpperCase() + " ✦";

        // Subtítulo con tag del territorio
        String subtitleText = info.secondaryColor + "« " + info.tag + " »";

        Component title = MessageUtils.colorizeLegacy(titleText);
        Component subtitle = MessageUtils.colorizeLegacy(subtitleText);

        Title.Times times = Title.Times.times(
                Duration.ofMillis(500),  // fade in
                Duration.ofMillis(4000), // stay
                Duration.ofMillis(1000)  // fade out
        );

        player.showTitle(Title.title(title, subtitle, times));

        // Enviar mensaje detallado en el chat
        sendDetailedTerritoryInfo(player, territory, info);
    }

    /**
     * Envía mensaje mejorado de entrada a territorio
     */
    private void sendEnhancedTerritoryMessage(Player player, Territory territory) {
        String factionColor = getFactionColor(territory);
        String titleText = factionColor + "§l✦ " + territory.getDisplayName().toUpperCase() + " ✦";
        String subtitleText = getStatusSubtitle(territory);

        Component title = MessageUtils.colorizeLegacy(titleText);
        Component subtitle = MessageUtils.colorizeLegacy(subtitleText);

        Title.Times times = Title.Times.times(
                Duration.ofMillis(500),
                Duration.ofMillis(4000),
                Duration.ofMillis(1000)
        );

        player.showTitle(Title.title(title, subtitle, times));

        // Mensaje mejorado con diseño griego/egipcio según facción
        player.sendMessage("");

        String borderStyle = getBorderStyle(territory);
        player.sendMessage(MessageUtils.colorizeLegacy(borderStyle));

        String territoryIcon = getTerritoryIcon(territory);
        player.sendMessage(MessageUtils.colorizeLegacy(factionColor + "§l    " + territoryIcon + " " + territory.getDisplayName().toUpperCase() + " " + territoryIcon));
        player.sendMessage("");

        // Descripción elegante del territorio
        String description = getDescriptionFromYaml(territory.getId());
        if (description != null) {
            player.sendMessage(MessageUtils.colorizeLegacy("  §8┃ §f§o" + description));
            player.sendMessage("");
        }

        // Información con iconos temáticos
        player.sendMessage(MessageUtils.colorizeLegacy("  §8┃ " + factionColor + "⚔ §7Nivel: §f" + territory.getLevel()));
        player.sendMessage(MessageUtils.colorizeLegacy("  §8┃ " + factionColor + "◆ §7Tamaño: §f" + territory.getSize().name()));

        String ownerStatus = territory.isOwned() ?
                "§c✖ Reclamado" :
                "§a✔ Disponible";
        player.sendMessage(MessageUtils.colorizeLegacy("  §8┃ " + factionColor + "♦ §7Estado: " + ownerStatus));

        player.sendMessage(MessageUtils.colorizeLegacy(borderStyle));
        player.sendMessage("");
    }

    /**
     * Envía información detallada del territorio en el chat
     */
    private void sendDetailedTerritoryInfo(Player player, Territory territory, TerritoryData.TerritoryInfo info) {
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorizeLegacy(info.primaryColor + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
        player.sendMessage(MessageUtils.colorizeLegacy(info.primaryColor + "§l        ✦ " + territory.getDisplayName().toUpperCase() + " ✦"));
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorizeLegacy("  §7" + info.description));
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorizeLegacy("  §e⚔ Nivel: §f" + territory.getLevel()));
        player.sendMessage(MessageUtils.colorizeLegacy("  §e⚡ Tag: §f" + info.tag));

        String ownerStatus = territory.isOwned() ?
                "§c✖ Reclamado por " + territory.getOwnerClanId() :
                "§a✔ Disponible para reclamar";
        player.sendMessage(MessageUtils.colorizeLegacy("  §e👑 Estado: " + ownerStatus));

        player.sendMessage(MessageUtils.colorizeLegacy(info.primaryColor + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
        player.sendMessage("");
    }

    private String getStatusSubtitle(Territory territory) {
        if (territory.isOwned()) {
            return "§c✖ §fReclamado §c✖";
        }
        return "§a✔ §fDisponible §a✔";
    }

    private String getFactionColor(Territory territory) {
        return switch (territory.getFaction()) {
            case GREECE -> "§3"; // Azul oscuro más elegante
            case EGYPT -> "§6";
            case NEUTRAL -> "§5";
        };
    }

    private String getBorderStyle(Territory territory) {
        return switch (territory.getFaction()) {
            case GREECE -> "§3§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━§r";
            case EGYPT -> "§6§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━§r";
            case NEUTRAL -> "§5§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━§r";
        };
    }

    private String getTerritoryIcon(Territory territory) {
        return switch (territory.getFaction()) {
            case GREECE -> "⚔";
            case EGYPT -> "☥";
            case NEUTRAL -> "⚡";
        };
    }

    private String getDescriptionFromYaml(String territoryId) {
        // Intentar obtener descripción desde el YAML de configuración
        return plugin.getConfig().getString("territory_descriptions." + territoryId, null);
    }

    private TerritoryData.TerritoryInfo getTerritoryInfo(String territoryId) {
        for (TerritoryData.TerritoryInfo info : TerritoryData.GREECE_TERRITORIES) {
            if (info.id.equals(territoryId)) return info;
        }
        for (TerritoryData.TerritoryInfo info : TerritoryData.EGYPT_TERRITORIES) {
            if (info.id.equals(territoryId)) return info;
        }
        for (TerritoryData.TerritoryInfo info : TerritoryData.CENTRAL_TERRITORIES) {
            if (info.id.equals(territoryId)) return info;
        }
        return null;
    }

    private Territory getTerritoryAt(Location loc) {
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            BoundingBox bbox = territory.getBoundingBox();

            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();

            if (x >= bbox.getX1() && x <= bbox.getX2() &&
                    y >= bbox.getY1() && y <= bbox.getY2() &&
                    z >= bbox.getZ1() && z <= bbox.getZ2()) {
                return territory;
            }
        }
        return null;
    }
}