package com.invictus.territory.manager;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.*;

/**
 * Gestor de mobs temáticos por territorio
 */
public class TerritoryMobManager implements Listener {

    private final TerritoryCore plugin;
    private final Map<String, List<MobSpawnData>> territoryMobs;
    private final int safeZoneRadius;
    private final boolean preventHostileSpawns;

    public TerritoryMobManager(TerritoryCore plugin) {
        this.plugin = plugin;
        this.territoryMobs = new HashMap<>();
        this.safeZoneRadius = plugin.getConfig().getInt("spawns.safe_zone_radius", 50);
        this.preventHostileSpawns = plugin.getConfig().getBoolean("spawns.prevent_hostile_spawns", true);

        loadTerritoryMobs();
    }

    /**
     * Carga los mobs temáticos desde la configuración
     */
    private void loadTerritoryMobs() {
        ConfigurationSection mobsSection = plugin.getConfig().getConfigurationSection("territory_mobs.custom_spawns");
        if (mobsSection == null) return;

        for (String territoryId : mobsSection.getKeys(false)) {
            List<String> mobList = plugin.getConfig().getStringList("territory_mobs.custom_spawns." + territoryId);
            List<MobSpawnData> spawnDataList = new ArrayList<>();

            for (String mobData : mobList) {
                try {
                    String[] parts = mobData.split(":");
                    if (parts.length >= 3) {
                        EntityType type = EntityType.valueOf(parts[0]);
                        int level = Integer.parseInt(parts[1]);
                        String customName = parts[2];

                        spawnDataList.add(new MobSpawnData(type, level, customName));
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("[TerritoryCore] ⚠️ Mob inválido en territorio " + territoryId + ": " + mobData);
                }
            }

            territoryMobs.put(territoryId, spawnDataList);
        }

        plugin.getLogger().info("[TerritoryCore] ✅ " + territoryMobs.size() + " territorios con mobs temáticos cargados");
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Location loc = event.getLocation();
        Entity entity = event.getEntity();

        // Encontrar territorio
        Territory territory = findTerritoryAt(loc);
        if (territory == null) return;

        // Verificar zona segura (spawn del territorio)
        if (isInSafeZone(loc, territory)) {
            if (preventHostileSpawns && entity instanceof Monster) {
                event.setCancelled(true);
                if (plugin.getConfig().getBoolean("debug.log_mob_spawns")) {
                    plugin.getLogger().info("[TerritoryCore] 🛡️ Mob bloqueado en zona segura: " + territory.getId());
                }
                return;
            }
        }

        // Reemplazar mob con versión temática si está configurado
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {

            replaceMobWithThematic(event, territory);
        }
    }

    /**
     * Reemplaza un mob con su versión temática del territorio
     */
    private void replaceMobWithThematic(CreatureSpawnEvent event, Territory territory) {
        List<MobSpawnData> possibleMobs = territoryMobs.get(territory.getId());
        if (possibleMobs == null || possibleMobs.isEmpty()) return;

        // Probabilidad de reemplazo (30%)
        if (Math.random() > 0.3) return;

        // Elegir mob aleatorio de la lista
        MobSpawnData mobData = possibleMobs.get(new Random().nextInt(possibleMobs.size()));

        // Reemplazar el mob
        event.setCancelled(true);
        Location loc = event.getLocation();

        LivingEntity newMob = (LivingEntity) loc.getWorld().spawnEntity(loc, mobData.type);
        if (newMob != null) {
            // Aplicar nombre personalizado
            Component customName = MessageUtils.colorizeLegacy(mobData.customName);
            newMob.customName(customName);
            newMob.setCustomNameVisible(true);

            // Aplicar nivel (aumentar salud y daño)
            double healthMultiplier = 1.0 + (mobData.level * 0.2);
            var maxHealthAttr = newMob.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double newMaxHealth = maxHealthAttr.getBaseValue() * healthMultiplier;
                maxHealthAttr.setBaseValue(newMaxHealth);
                newMob.setHealth(newMaxHealth);
            }

            if (plugin.getConfig().getBoolean("debug.log_mob_spawns")) {
                plugin.getLogger().info("[TerritoryCore] 🎭 Mob temático spawneado en " + territory.getId() + ": " + mobData.customName);
            }
        }
    }

    /**
     * Verifica si una ubicación está en la zona segura del spawn
     */
    private boolean isInSafeZone(Location loc, Territory territory) {
        Location center = territory.getBoundingBox().getCenter();
        double distance = loc.distance(center);
        return distance <= safeZoneRadius;
    }

    /**
     * Encuentra el territorio en una ubicación
     */
    private Territory findTerritoryAt(Location loc) {
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            if (territory.getBoundingBox().isInside(loc)) {
                return territory;
            }
        }
        return null;
    }

    /**
     * Clase para almacenar datos de spawn de mob
     */
    private static class MobSpawnData {
        final EntityType type;
        final int level;
        final String customName;

        MobSpawnData(EntityType type, int level, String customName) {
            this.type = type;
            this.level = level;
            this.customName = customName;
        }
    }
}
