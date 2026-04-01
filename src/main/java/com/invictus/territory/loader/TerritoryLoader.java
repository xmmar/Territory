package com.invictus.territory.loader;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.BoundingBox;
import com.invictus.territory.model.Territory;
import com.invictus.territory.model.enums.TerritoryCategory;
import com.invictus.territory.model.enums.TerritoryFaction;
import com.invictus.territory.model.enums.TerritorySize;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Cargador de territorios desde territories.yml
 */
public class TerritoryLoader {

    private final TerritoryCore plugin;
    private final File territoriesFile;

    public TerritoryLoader(TerritoryCore plugin) {
        this.plugin = plugin;
        this.territoriesFile = new File(plugin.getDataFolder(), "territories.yml");
    }

    /**
     * Carga todos los territorios desde territories.yml
     *
     * @return Lista de territorios cargados
     */
    public List<Territory> loadTerritories() {
        List<Territory> territories = new ArrayList<>();

        if (!territoriesFile.exists()) {
            plugin.getLogger().severe("[TerritoryCore] ❌ territories.yml no encontrado");
            return territories;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(territoriesFile);

        // Cargar territorios griegos
        ConfigurationSection greeceSection = config.getConfigurationSection("greece");
        if (greeceSection != null) {
            territories.addAll(loadTerritoriesFromSection(greeceSection, TerritoryFaction.GREECE));
        }

        // Cargar territorios egipcios
        ConfigurationSection egyptSection = config.getConfigurationSection("egypt");
        if (egyptSection != null) {
            territories.addAll(loadTerritoriesFromSection(egyptSection, TerritoryFaction.EGYPT));
        }

        // Cargar territorios centrales
        ConfigurationSection centerSection = config.getConfigurationSection("center");
        if (centerSection != null) {
            territories.addAll(loadTerritoriesFromSection(centerSection, TerritoryFaction.NEUTRAL));
        }

        // Cargar el Trono
        ConfigurationSection throneSection = config.getConfigurationSection("throne");
        if (throneSection != null) {
            territories.addAll(loadTerritoriesFromSection(throneSection, TerritoryFaction.NEUTRAL));
        }

        plugin.getLogger().info("[TerritoryCore] ✅ " + territories.size() + " territorios cargados desde territories.yml");
        return territories;
    }

    /**
     * Carga territorios desde una sección de configuración
     *
     * @param section Sección de configuración
     * @param faction Facción de los territorios
     * @return Lista de territorios cargados
     */
    private List<Territory> loadTerritoriesFromSection(ConfigurationSection section, TerritoryFaction faction) {
        List<Territory> territories = new ArrayList<>();

        for (String territoryKey : section.getKeys(false)) {
            ConfigurationSection territorySection = section.getConfigurationSection(territoryKey);
            if (territorySection == null) continue;

            try {
                Territory territory = loadTerritory(territorySection, faction);
                if (territory != null) {
                    territories.add(territory);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("[TerritoryCore] ⚠️ Error cargando territorio: " + territoryKey);
                e.printStackTrace();
            }
        }

        return territories;
    }

    /**
     * Carga un territorio individual desde una sección
     *
     * @param section Sección del territorio
     * @param faction Facción del territorio
     * @return Territorio cargado o null si hay error
     */
    private Territory loadTerritory(ConfigurationSection section, TerritoryFaction faction) {
        String id = section.getString("id");
        String displayName = section.getString("display_name");
        String categoryStr = section.getString("category");
        String sizeStr = section.getString("size");
        int level = section.getInt("level", 1);
        long upkeep = section.getLong("upkeep");
        long production = section.getLong("production");
        String description = section.getString("description");
        String region = section.getString("region");

        if (id == null || displayName == null) {
            plugin.getLogger().warning("[TerritoryCore] ⚠️ Territorio sin ID o nombre: " + section.getName());
            return null;
        }

        TerritoryCategory category;
        try {
            category = TerritoryCategory.valueOf(categoryStr);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("[TerritoryCore] ⚠️ Categoría inválida para " + id + ": " + categoryStr);
            category = TerritoryCategory.STRATEGIC;
        }

        TerritorySize size;
        try {
            size = TerritorySize.valueOf(sizeStr);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("[TerritoryCore] ⚠️ Tamaño inválido para " + id + ": " + sizeStr);
            size = TerritorySize.MEDIUM;
        }

        // Crear BoundingBox temporal (debe ser configurado después por admin)
        String worldName = plugin.getConfig().getString("world", "CHRONICLE");
        Location center = new Location(plugin.getServer().getWorld(worldName), 0, 64, 0);
        int radius = size.getBaseRadius();
        Location min = center.clone().subtract(radius, 0, radius);
        Location max = center.clone().add(radius, 256, radius);
        BoundingBox boundingBox = new BoundingBox(min, max);

        Territory territory = new Territory(id, displayName, faction, category, size, boundingBox);
        territory.setLevel(level);
        territory.setUpkeepBase(upkeep);
        territory.setProduction(production);

        return territory;
    }
}
