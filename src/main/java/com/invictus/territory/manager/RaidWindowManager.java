package com.invictus.territory.manager;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import org.bukkit.entity.Player;

import java.util.*;

public class RaidWindowManager {

    private final TerritoryCore plugin;
    private final Map<String, Long> raidWindows = new HashMap<>();

    public RaidWindowManager(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    /**
     * Comprueba si un territorio está en disputa
     */
    public boolean isInDispute(String territoryId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        return territory != null && territory.isOwned();
    }

    /**
     * Obtiene la ventana de raid de un territorio
     */
    public long getRaidWindow(String territoryId) {
        return raidWindows.getOrDefault(territoryId, System.currentTimeMillis());
    }

    /**
     * Obtiene el último raid de un territorio
     */
    public long getLastRaidAt(String territoryId) {
        return raidWindows.getOrDefault(territoryId, 0L);
    }

    /**
     * Inicia un raid en un territorio
     */
    public void startRaid(String territoryId, Player raider) {
        raidWindows.put(territoryId, System.currentTimeMillis());
        plugin.getLogger().info("[TerritoryCore] 🏴 Raid iniciado en: " + territoryId);
    }

    /**
     * Finaliza un raid
     */
    public void endRaid(String territoryId) {
        raidWindows.remove(territoryId);
        plugin.getLogger().info("[TerritoryCore] ✅ Raid finalizado en: " + territoryId);
    }
}