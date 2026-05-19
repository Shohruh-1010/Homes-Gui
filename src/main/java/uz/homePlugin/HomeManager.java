package uz.homePlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HomeManager {

    private final HomePlugin plugin;
    private final Map<UUID, List<Home>> homes = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public static final int MAX_HOMES = 10;

    public HomeManager(HomePlugin plugin) {
        this.plugin = plugin;
        dataFile = new File(plugin.getDataFolder(), "homes.yml");
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadHomes() {
        homes.clear();
        if (!dataConfig.contains("homes")) return;
        for (String uuidStr : dataConfig.getConfigurationSection("homes").getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            List<Home> playerHomes = new ArrayList<>();
            for (String homeName : dataConfig.getConfigurationSection("homes." + uuidStr).getKeys(false)) {
                String data = dataConfig.getString("homes." + uuidStr + "." + homeName);
                playerHomes.add(Home.deserialize(homeName, data));
            }
            homes.put(uuid, playerHomes);
        }
    }

    public void saveHomes() {
        for (Map.Entry<UUID, List<Home>> entry : homes.entrySet()) {
            for (Home home : entry.getValue()) {
                dataConfig.set("homes." + entry.getKey() + "." + home.getName(), home.serialize());
            }
        }
        try { dataConfig.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public List<Home> getHomes(Player player) {
        return homes.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public Home getHome(Player player, String name) {
        for (Home h : getHomes(player)) {
            if (h.getName().equalsIgnoreCase(name)) return h;
        }
        return null;
    }

    public boolean addHome(Player player, String name) {
        List<Home> list = homes.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        if (list.size() >= MAX_HOMES) return false;
        list.removeIf(h -> h.getName().equalsIgnoreCase(name));
        list.add(new Home(name, player.getLocation().clone()));
        saveHomes();
        return true;
    }

    public boolean removeHome(Player player, String name) {
        List<Home> list = getHomes(player);
        boolean removed = list.removeIf(h -> h.getName().equalsIgnoreCase(name));
        if (removed) {
            homes.put(player.getUniqueId(), list);
            dataConfig.set("homes." + player.getUniqueId() + "." + name, null);
            try { dataConfig.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
        }
        return removed;
    }

    public int getHomeCount(Player player) {
        return getHomes(player).size();
    }
}
