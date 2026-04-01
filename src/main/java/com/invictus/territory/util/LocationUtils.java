package com.invictus.territory.util;

import org.bukkit.Location;

public class LocationUtils {

    public static String locationToString(Location loc) {
        return String.format("%s: %d, %d, %d",
                loc.getWorld().getName(),
                loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ()
        );
    }

    public static Location stringToLocation(String str) {
        // TODO: Parsear string a Location
        return null;
    }
}