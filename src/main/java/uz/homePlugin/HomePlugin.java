package uz.homePlugin;

import org.bukkit.plugin.java.JavaPlugin;

public class HomePlugin extends JavaPlugin {

    private HomeManager homeManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        homeManager = new HomeManager(this);
        homeManager.loadHomes();

        getCommand("homes").setExecutor(new HomeCommand(this));
        getLogger().info("HomePlugin yoqildi!");
    }

    @Override
    public void onDisable() {
        homeManager.saveHomes();
        getLogger().info("HomePlugin o'chirildi!");
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }
}
