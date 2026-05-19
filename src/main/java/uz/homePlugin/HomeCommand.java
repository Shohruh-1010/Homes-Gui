package uz.homePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    private final HomePlugin plugin;
    private final HomeGUI gui;

    public HomeCommand(HomePlugin plugin) {
        this.plugin = plugin;
        this.gui = new HomeGUI(plugin);
        plugin.getServer().getPluginManager().registerEvents(
            new HomeGUIListener(plugin, gui), plugin
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu buyruq faqat o'yinchilar uchun!");
            return true;
        }
        Player player = (Player) sender;
        gui.openMainGUI(player);
        return true;
    }
}
