package com.invictus.territory.generator;

import com.invictus.territory.data.TerritoryData;
import com.invictus.territory.model.BoundingBox;
import com.invictus.territory.model.Territory;
import com.invictus.territory.model.enums.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.*;

public class BiomeGenerator {

    private final World world;
    private final int MIN_DISTANCE = 800;
    private final int TERRITORY_SIZE = 250;
    private final Random random = new Random();

    public BiomeGenerator(World world) {
        this.world = world;
    }

    public List<Territory> generateTerritories() {
        List<Territory> territories = new ArrayList<>();
        List<BoundingBox> usedLocations = new ArrayList<>();

        // Generar territorios de Grecia
        for (TerritoryData.TerritoryInfo info : TerritoryData.GREECE_TERRITORIES) {
            Territory territory = generateTerritoryInBiome(info, usedLocations);
            if (territory != null) {
                territories.add(territory);
                Bukkit.getLogger().info("[TerritoryCore] ✅ Territorio generado: " + info.displayName + " en " +
                        territory.getBoundingBox().getX1() + ", " + territory.getBoundingBox().getZ1());
            }
        }

        // Generar territorios de Egipto
        for (TerritoryData.TerritoryInfo info : TerritoryData.EGYPT_TERRITORIES) {
            Territory territory = generateTerritoryInBiome(info, usedLocations);
            if (territory != null) {
                territories.add(territory);
                Bukkit.getLogger().info("[TerritoryCore] ✅ Territorio generado: " + info.displayName + " en " +
                        territory.getBoundingBox().getX1() + ", " + territory.getBoundingBox().getZ1());
            }
        }

        // Generar territorios Centrales
        for (TerritoryData.TerritoryInfo info : TerritoryData.CENTRAL_TERRITORIES) {
            Territory territory = generateTerritoryInBiome(info, usedLocations);
            if (territory != null) {
                territories.add(territory);
                Bukkit.getLogger().info("[TerritoryCore] ✅ " +
                        (info.number == 25 ? "El Trono generado" : "Territorio generado: " + info.displayName) +
                        " en " + territory.getBoundingBox().getX1() + ", " + territory.getBoundingBox().getZ1());
            }
        }

        return territories;
    }

    private Territory generateTerritoryInBiome(TerritoryData.TerritoryInfo info, List<BoundingBox> usedLocations) {
        int attempts = 0;
        int maxAttempts = 500;

        while (attempts < maxAttempts) {
            attempts++;

            int x = random.nextInt(40000) - 20000;
            int z = random.nextInt(40000) - 20000;

            if (!isBiomeAt(x, z, info.biome)) {
                continue;
            }

            Location min = new Location(world, x - TERRITORY_SIZE, 0, z - TERRITORY_SIZE);
            Location max = new Location(world, x + TERRITORY_SIZE, 256, z + TERRITORY_SIZE);
            BoundingBox bbox = new BoundingBox(min, max);

            if (!isValidDistance(bbox, usedLocations)) {
                continue;
            }

            TerritoryFaction faction;
            if (info.number <= 10) {
                faction = TerritoryFaction.GREECE;
            } else if (info.number <= 20) {
                faction = TerritoryFaction.EGYPT;
            } else {
                faction = TerritoryFaction.NEUTRAL;
            }

            Territory territory = new Territory(
                    info.id,
                    info.displayName,
                    selectSize(info.number),
                    info.number == 25 ? TerritoryCategory.CAPITAL : TerritoryCategory.STRATEGIC,
                    faction,
                    bbox
            );

            usedLocations.add(bbox);
            return territory;
        }

        return null;
    }

    private boolean isValidDistance(BoundingBox newBox, List<BoundingBox> usedLocations) {
        int newCenterX = (int) ((newBox.getX1() + newBox.getX2()) / 2);
        int newCenterZ = (int) ((newBox.getZ1() + newBox.getZ2()) / 2);

        for (BoundingBox used : usedLocations) {
            int usedCenterX = (int) ((used.getX1() + used.getX2()) / 2);
            int usedCenterZ = (int) ((used.getZ1() + used.getZ2()) / 2);

            double distance = Math.sqrt(
                    Math.pow(newCenterX - usedCenterX, 2) +
                            Math.pow(newCenterZ - usedCenterZ, 2)
            );

            if (distance < MIN_DISTANCE) {
                return false;
            }
        }

        return true;
    }

    private boolean isBiomeAt(int x, int z, Biome targetBiome) {
        try {
            Biome biome = world.getBiome(x, 64, z);
            return biome == targetBiome;
        } catch (Exception e) {
            return false;
        }
    }

    private TerritorySize selectSize(int index) {
        if (index <= 5 || (index >= 11 && index <= 15) || (index >= 21 && index <= 23)) {
            return TerritorySize.LARGE;
        } else if (index <= 8 || (index >= 16 && index <= 18)) {
            return TerritorySize.MEDIUM;
        } else {
            return TerritorySize.SMALL;
        }
    }
}