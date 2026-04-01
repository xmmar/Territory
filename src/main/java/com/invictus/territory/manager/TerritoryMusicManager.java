package com.invictus.territory.manager;

import com.invictus.territory.data.TerritoryData;
import com.invictus.territory.model.Territory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TerritoryMusicManager {

    /**
     * Toca la música de entrada al territorio (5 segundos)
     */
    public static void playTerritoryMusic(Player player, Territory territory) {
        TerritoryData.TerritoryInfo info = getTerritoryInfo(territory.getId());

        if (info == null) return;

        Location loc = player.getLocation();
        player.playSound(loc, info.ambientSound, 2.0f, info.soundPitch);
    }

    /**
     * Obtiene la información del territorio
     */
    private static TerritoryData.TerritoryInfo getTerritoryInfo(String territoryId) {
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
}