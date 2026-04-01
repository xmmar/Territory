package com.invictus.territory.api;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;

import java.util.UUID;

/**
 * API pública para TerritoryCore
 */
public class TerritoryAPI {

    private final TerritoryCore plugin;

    public TerritoryAPI(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    /**
     * Obtiene un territorio por su ID
     */
    public Territory getTerritory(String territoryId) {
        return plugin.getTerritoryManager().getTerritory(territoryId);
    }

    /**
     * Comprueba si un jugador puede construir en un territorio
     */
    public boolean canBuild(String territoryId, UUID playerId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        if (territory == null) return true;
        if (!territory.isOwned()) return true;
        return territory.getOwnerClanId().equals(playerId);
    }

    /**
     * Incrementa el nivel de un territorio
     */
    public void incrementLevel(String territoryId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        if (territory != null) {
            territory.setLevel(territory.getLevel() + 1);
        }
    }
}