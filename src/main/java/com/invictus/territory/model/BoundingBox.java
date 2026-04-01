package com.invictus.territory.model;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class BoundingBox {
    private Location min;
    private Location max;
    private World world;

    public BoundingBox(Location min, Location max) {
        this.min = min;
        this.max = max;
        this.world = min.getWorld();
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public World getWorld() {
        return world;
    }

    // Métodos individuales de coordenadas
    public double getX1() {
        return min.getX();
    }

    public double getX2() {
        return max.getX();
    }

    public double getY1() {
        return min.getY();
    }

    public double getY2() {
        return max.getY();
    }

    public double getZ1() {
        return min.getZ();
    }

    public double getZ2() {
        return max.getZ();
    }

    /**
     * Obtiene el centro del BoundingBox
     */
    public Location getCenter() {
        double centerX = (min.getX() + max.getX()) / 2;
        double centerY = (min.getY() + max.getY()) / 2;
        double centerZ = (min.getZ() + max.getZ()) / 2;

        return new Location(world, centerX, centerY, centerZ);
    }

    /**
     * Comprueba si una ubicación está dentro del BoundingBox
     */
    public boolean isInside(Location location) {
        if (!location.getWorld().equals(world)) {
            return false;
        }

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= min.getX() && x <= max.getX() &&
                y >= min.getY() && y <= max.getY() &&
                z >= min.getZ() && z <= max.getZ();
    }

    /**
     * Obtiene el tamaño del BoundingBox
     */
    public int getSize() {
        int sizeX = (int) Math.abs(max.getX() - min.getX());
        int sizeZ = (int) Math.abs(max.getZ() - min.getZ());
        return Math.max(sizeX, sizeZ);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "min=" + min +
                ", max=" + max +
                ", world=" + world.getName() +
                '}';
    }
}