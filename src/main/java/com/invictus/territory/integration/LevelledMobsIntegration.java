package com.invictus.territory.integration;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class LevelledMobsIntegration implements Listener {

    private final TerritoryCore plugin;

    public LevelledMobsIntegration(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        Territory territory = getTerritoryAt(entity);

        if (territory == null) return;

        // Escalar mobs según el nivel del territorio
        int mobLevel = calculateMobLevel(territory);

        // Aquí puedes usar la API de LevelledMobs para setear el nivel
        // Ejemplo (pseudocódigo):
        // LevelledMobs.getInstance().getMobManager().setLevel(entity, mobLevel);

        Component customName = MessageUtils.colorizeLegacy("§e[Lvl " + mobLevel + "] " + entity.getName());
        entity.customName(customName);
        entity.setCustomNameVisible(true);
    }

    /**
     * Calcula el nivel del mob según el territorio
     */
    private int calculateMobLevel(Territory territory) {
        return switch (territory.getId()) {
            case "monte_olimpo" -> 10;
            case "esparta" -> 25;
            case "bosque_artemisa" -> 20;
            case "atenas_antigua" -> 15;
            case "acantilados_poseidon" -> 18;
            case "santuario_apolo" -> 16;
            case "ruinas_delfos" -> 22;
            case "valle_ares" -> 28;
            case "coloso_rodas" -> 50; // Boss
            case "islas_egeas" -> 30;
            case "piramide_keops" -> 35;
            case "valle_reyes" -> 40;
            case "oasis_bastet" -> 12;
            case "desierto_set" -> 35;
            case "templo_anubis" -> 38;
            case "rio_nilo" -> 18;
            case "biblioteca_thoth" -> 20;
            case "necropolis" -> 55; // Boss
            case "templo_solar_ra" -> 25;
            case "laberinto_sobek" -> 45;
            case "ruinas_imperio" -> 60;
            case "arena_titanes" -> 50;
            case "puerta_inframundo" -> 55;
            case "ciudad_perdida" -> 52;
            case "trono_dominador" -> 100; // Super Boss
            default -> 10;
        };
    }

    private Territory getTerritoryAt(LivingEntity entity) {
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            com.invictus.territory.model.BoundingBox bbox = territory.getBoundingBox();

            int x = entity.getLocation().getBlockX();
            int y = entity.getLocation().getBlockY();
            int z = entity.getLocation().getBlockZ();

            if (x >= bbox.getX1() && x <= bbox.getX2() &&
                    y >= bbox.getY1() && y <= bbox.getY2() &&
                    z >= bbox.getZ1() && z <= bbox.getZ2()) {
                return territory;
            }
        }
        return null;
    }
}