package uz.homePlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Home {
    private final String name;
    private final Location location;

    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String serialize() {
        return location.getWorld().getName() + "," +
               location.getX() + "," +
               location.getY() + "," +
               location.getZ() + "," +
               location.getYaw() + "," +
               location.getPitch();
    }

    public static Home deserialize(String name, String data) {
        String[] parts = data.split(",");
        Location loc = new Location(
            Bukkit.getWorld(parts[0]),
            Double.parseDouble(parts[1]),
            Double.parseDouble(parts[2]),
            Double.parseDouble(parts[3]),
            Float.parseFloat(parts[4]),
            Float.parseFloat(parts[5])
        );
        return new Home(name, loc);
    }
}
