package com.invictus.territory.manager;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;

import java.util.UUID;

/**
 * Manager para manejar claims/reclamaciones de territorios
 */
public class TerritoryClaimManager {
    
    private final TerritoryCore plugin;
    
    public TerritoryClaimManager(TerritoryCore plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Reclama un territorio para un clan
     * @param territoryId ID del territorio
     * @param clanId UUID del clan
     * @return true si se reclamó exitosamente, false si no
     */
    public boolean claimTerritory(String territoryId, UUID clanId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory == null) {
            plugin.getLogger().warning("[TerritoryCore] Territorio no encontrado: " + territoryId);
            return false;
        }
        
        if (territory.isOwned()) {
            plugin.getLogger().warning("[TerritoryCore] El territorio ya está reclamado: " + territoryId);
            return false;
        }
        
        territory.setOwned(true);
        territory.setOwnerClanId(clanId);
        
        plugin.getLogger().info("[TerritoryCore] 🏴 Territorio reclamado: " + territoryId + " por clan: " + clanId);
        return true;
    }
    
    /**
     * Libera un territorio (solo si el clan propietario lo solicita)
     * @param territoryId ID del territorio
     * @param clanId UUID del clan que lo posee
     * @return true si se liberó exitosamente, false si no
     */
    public boolean unclaimTerritory(String territoryId, UUID clanId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory == null) {
            plugin.getLogger().warning("[TerritoryCore] Territorio no encontrado: " + territoryId);
            return false;
        }
        
        if (!territory.isOwned()) {
            plugin.getLogger().warning("[TerritoryCore] El territorio no está reclamado: " + territoryId);
            return false;
        }
        
        if (!territory.getOwnerClanId().equals(clanId)) {
            plugin.getLogger().warning("[TerritoryCore] El clan no es propietario del territorio: " + territoryId);
            return false;
        }
        
        territory.setOwned(false);
        territory.setOwnerClanId(null);
        
        plugin.getLogger().info("[TerritoryCore] ⚪ Territorio liberado: " + territoryId);
        return true;
    }
    
    /**
     * Fuerza la liberación de un territorio (sin verificar propietario)
     * @param territoryId ID del territorio
     */
    public void forceUnclaimTerritory(String territoryId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory == null) {
            plugin.getLogger().warning("[TerritoryCore] Territorio no encontrado: " + territoryId);
            return;
        }
        
        territory.setOwned(false);
        territory.setOwnerClanId(null);
        
        plugin.getLogger().info("[TerritoryCore] ⚪ Territorio forzosamente liberado: " + territoryId);
    }
    
    /**
     * Transfiere un territorio a otro clan
     * @param territoryId ID del territorio
     * @param newClanId UUID del nuevo dueño
     * @return true si se transfirió exitosamente, false si no
     */
    public boolean transferTerritory(String territoryId, UUID newClanId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory == null) {
            plugin.getLogger().warning("[TerritoryCore] Territorio no encontrado: " + territoryId);
            return false;
        }
        
        UUID oldClanId = territory.getOwnerClanId();
        territory.setOwnerClanId(newClanId);
        
        plugin.getLogger().info("[TerritoryCore] 🔄 Territorio transferido: " + territoryId + 
                              " de " + oldClanId + " a " + newClanId);
        return true;
    }
    
    /**
     * Actualiza la información de un territorio
     * @param territoryId ID del territorio
     */
    public void updateTerritory(String territoryId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory != null) {
            plugin.getLogger().info("[TerritoryCore] 🔄 Territorio actualizado: " + territoryId);
        } else {
            plugin.getLogger().warning("[TerritoryCore] Territorio no encontrado para actualizar: " + territoryId);
        }
    }
    
    /**
     * Resetea el upkeep de un territorio
     * @param territoryId ID del territorio
     */
    public void resetUpkeepStreak(String territoryId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory != null) {
            territory.setUpkeepBase(1000);
            plugin.getLogger().info("[TerritoryCore] 💰 Upkeep reseteado: " + territoryId);
        } else {
            plugin.getLogger().warning("[TerritoryCore] Territorio no encontrado para resetear upkeep: " + territoryId);
        }
    }
    
    /**
     * Incrementa el nivel de un territorio
     * @param territoryId ID del territorio
     */
    public void incrementLevel(String territoryId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory != null) {
            int newLevel = territory.getLevel() + 1;
            territory.setLevel(newLevel);
            plugin.getLogger().info("[TerritoryCore] ⬆️ Nivel incrementado en " + territoryId + 
                                  " a nivel: " + newLevel);
        } else {
            plugin.getLogger().warning("[TerritoryCore] Territorio no encontrado para incrementar nivel: " + territoryId);
        }
    }
    
    /**
     * Comprueba si un clan es propietario de un territorio
     * @param territoryId ID del territorio
     * @param clanId UUID del clan
     * @return true si el clan es propietario, false si no
     */
    public boolean isClanOwner(String territoryId, UUID clanId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory == null) return false;
        if (!territory.isOwned()) return false;
        
        return territory.getOwnerClanId().equals(clanId);
    }
    
    /**
     * Obtiene el propietario de un territorio
     * @param territoryId ID del territorio
     * @return UUID del clan propietario, o null si no está reclamado
     */
    public UUID getTerritoryOwner(String territoryId) {
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
        
        if (territory == null || !territory.isOwned()) {
            return null;
        }
        
        return territory.getOwnerClanId();
    }
}