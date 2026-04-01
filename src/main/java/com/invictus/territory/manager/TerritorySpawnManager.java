package com.invictus.territory.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TerritorySpawnManager {

    /**
     * Obtiene una ubicación segura para spawnear al jugador
     * Busca el primer bloque sólido desde arriba
     */
    public static Location getSafeSpawnLocation(Location targetLocation) {
        World world = targetLocation.getWorld();
        int x = targetLocation.getBlockX();
        int z = targetLocation.getBlockZ();

        // Buscar desde y=255 hacia abajo
        for (int y = 255; y >= 0; y--) {
            Block block = world.getBlockAt(x, y, z);
            Block blockAbove = world.getBlockAt(x, y + 1, z);

            // Si el bloque es sólido y el de arriba es aire, es un buen spawn
            if (isSolid(block) && !isSolid(blockAbove)) {
                return new Location(
                        world,
                        x + 0.5,
                        y + 1.5,
                        z + 0.5,
                        targetLocation.getYaw(),
                        targetLocation.getPitch()
                );
            }
        }

        // Si no encuentra nada, spawnea a Y=64
        return new Location(world, x + 0.5, 64.5, z + 0.5);
    }

    private static boolean isSolid(Block block) {
        return block.getType().isSolid() &&
                block.getType() != Material.WATER &&
                block.getType() != Material.LAVA;
    }
}