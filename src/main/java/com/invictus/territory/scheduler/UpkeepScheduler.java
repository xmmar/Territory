package com.invictus.territory.scheduler;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Scheduler que maneja el upkeep diario de territorios
 */
public class UpkeepScheduler extends BukkitRunnable {

    private final TerritoryCore plugin;

    public UpkeepScheduler(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Ejecutar cada día (20 ticks = 1 segundo, 1728000 ticks = 24 horas)
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            if (territory.isOwned()) {
                // Calcular upkeep
                int upkeepCost = calculateUpkeep(territory);
                plugin.getLogger().info("[TerritoryCore] 💰 Upkeep de " + territory.getDisplayName() + ": $" + upkeepCost);
            }
        }
    }

    private int calculateUpkeep(Territory territory) {
        long baseUpkeep = territory.getUpkeepBase();
        long levelMultiplier = territory.getLevel() * 100L;
        return (int) (baseUpkeep + levelMultiplier);
    }

    /**
     * Inicia el scheduler de upkeep (cada 24 horas)
     */
    public void start() {
        this.runTaskTimer(plugin, 1728000L, 1728000L); // Cada 24 horas
        plugin.getLogger().info("[TerritoryCore] ✅ Upkeep Scheduler iniciado");
    }
}