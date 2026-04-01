package com.invictus.territory.listener;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.event.*;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;

public class TerritoryEventListener implements Listener {

    public TerritoryEventListener(TerritoryCore territoryCore) {
    }

    @EventHandler
    public void onTerritoryClaim(TerritoryClaimEvent event) {
        Bukkit.getLogger().info(String.format(
                "[TerritoryCore] Territorio %s reclamado por clan %s",
                event.getTerritoryId(), event.getClanId()
        ));
    }

    @EventHandler
    public void onTerritoryUpkeep(TerritoryUpkeepEvent event) {
        String status = event.isSuccessful() ? "✅ PAGADO" : "❌ FALLIDO";
        Bukkit.getLogger().info(String.format(
                "[TerritoryCore] Upkeep para clan %s: %s ($%d)",
                event.getClanId(), status, event.getTotalCost()
        ));
    }

    @EventHandler
    public void onTerritoryEnter(TerritoryEnterEvent event) {
        event.getPlayer().sendMessage("📍 Has entrado en territorio: " + event.getTerritoryId());
    }
}