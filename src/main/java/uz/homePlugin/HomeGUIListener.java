package uz.homePlugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HomeGUIListener implements Listener {

    private final HomePlugin plugin;
    private final HomeGUI gui;

    public HomeGUIListener(HomePlugin plugin, HomeGUI gui) {
        this.plugin = plugin;
        this.gui = gui;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();

        if (title.equals(ChatColor.DARK_BLUE + "✦ Mening Homelarim ✦")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;

            int slot = e.getRawSlot();

            String bedHomeName = HomeGUI.getHomeNameByBedSlot(slot);
            if (bedHomeName != null) {
                Home home = plugin.getHomeManager().getHome(player, bedHomeName);
                if (home != null) {
                    player.closeInventory();
                    player.teleport(home.getLocation());
                    player.sendMessage(ChatColor.AQUA + "✦ " + bedHomeName + " ga teleport qilindi!");
                }
                return;
            }

            String dyeHomeName = HomeGUI.getHomeNameByDyeSlot(slot);
            if (dyeHomeName != null) {
                Home existing = plugin.getHomeManager().getHome(player, dyeHomeName);
                if (existing == null) {
                    plugin.getHomeManager().addHome(player, dyeHomeName);
                    player.sendMessage(ChatColor.GREEN + "✔ " + dyeHomeName + " saqlandi!");
                    player.closeInventory();
                    gui.openMainGUI(player);
                } else {
                    player.closeInventory();
                    gui.openConfirmDeleteGUI(player, dyeHomeName);
                }
            }
        }

        else if (title.equals(ChatColor.RED + "O'chirishni tasdiqlang")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            int slot = e.getRawSlot();

            if (slot == 16) {
                if (player.hasMetadata("pendingDelete")) {
                    String homeName = player.getMetadata("pendingDelete").get(0).asString();
                    plugin.getHomeManager().removeHome(player, homeName);
                    player.removeMetadata("pendingDelete", plugin);
                    player.sendMessage(ChatColor.RED + "✖ " + homeName + " o'chirildi.");
                    player.closeInventory();
                    gui.openMainGUI(player);
                }
            } else if (slot == 10) {
                player.removeMetadata("pendingDelete", plugin);
                player.closeInventory();
                gui.openMainGUI(player);
            }
        }
    }
}
