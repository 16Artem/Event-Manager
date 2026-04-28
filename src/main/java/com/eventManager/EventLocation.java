package com.eventManager;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Immutable value object representing a world location for event spawning.
 */
public class EventLocation {
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;

    public EventLocation(String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorldName() {
        return worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }
}
