package uz.homePlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class HomeGUI {

    private final HomePlugin plugin;

    private static final int[] BED_SLOTS  = {10, 11, 12, 13, 14, 28, 29, 30, 31, 32};
    private static final int[] DYE_SLOTS  = {19, 20, 21, 22, 23, 37, 38, 39, 40, 41};

    private static final String[] HOME_NAMES = {
        "home1","home2","home3","home4","home5",
        "home6","home7","home8","home9","home10"
    };

    public HomeGUI(HomePlugin plugin) {
        this.plugin = plugin;
    }

    public void openMainGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_BLUE + "✦ Mening Homelarim ✦");

        ItemStack filler = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) inv.setItem(i, filler);

        List<Home> playerHomes = plugin.getHomeManager().getHomes(player);

        for (int i = 0; i < 10; i++) {
            String homeName = HOME_NAMES[i];
            Home home = findHome(playerHomes, homeName);
            boolean hasHome = home != null;

            Material bedMat = hasHome ? Material.CYAN_BED : Material.BLACK_BED;
            String bedName = hasHome
                ? ChatColor.AQUA + "✦ " + homeName.toUpperCase()
                : ChatColor.DARK_GRAY + "✗ Bo'sh";
            ItemStack bed = makeItem(bedMat, bedName,
                hasHome ? Arrays.asList(
                    ChatColor.GRAY + "Dunyo: " + ChatColor.WHITE + home.getLocation().getWorld().getName(),
                    ChatColor.GRAY + "X: " + ChatColor.WHITE + (int)home.getLocation().getX(),
                    ChatColor.GRAY + "Y: " + ChatColor.WHITE + (int)home.getLocation().getY(),
                    ChatColor.GRAY + "Z: " + ChatColor.WHITE + (int)home.getLocation().getZ(),
                    "",
                    ChatColor.YELLOW + "▶ Bosing: teleport qilish"
                ) : Arrays.asList(ChatColor.DARK_GRAY + "Home qo'yilmagan")
            );
            inv.setItem(BED_SLOTS[i], bed);

            Material dyeMat = hasHome ? Material.CYAN_DYE : Material.GRAY_DYE;
            String dyeName = hasHome
                ? ChatColor.RED + "✖ O'chirish / " + ChatColor.GREEN + "✔ Saqlash"
                : ChatColor.GREEN + "✚ Home qo'yish";
            List<String> dyeLore = hasHome
                ? Arrays.asList(
                    ChatColor.GRAY + "Bosing: " + ChatColor.RED + "o'chirish",
                    ChatColor.DARK_GRAY + "(tasdiqlash kerak bo'ladi)"
                )
                : Arrays.asList(ChatColor.GRAY + "Bosing: joriy joyni saqlaydi");
            ItemStack dye = makeItem(dyeMat, dyeName, dyeLore);
            inv.setItem(DYE_SLOTS[i], dye);
        }

        player.openInventory(inv);
    }

    public void openConfirmDeleteGUI(Player player, String homeName) {
        Inventory inv = Bukkit.createInventory(null, 27,
            ChatColor.RED + "O'chirishni tasdiqlang");

        ItemStack filler = makeItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 27; i++) inv.setItem(i, filler);

        ItemStack confirm = makeItem(Material.LIME_STAINED_GLASS_PANE,
            ChatColor.GREEN + "✔ Ha, o'chirish",
            Arrays.asList(
                ChatColor.GRAY + "Home: " + ChatColor.WHITE + homeName,
                ChatColor.RED + "Bu amalini qaytarib bo'lmaydi!"
            ));
        inv.setItem(16, confirm);

        ItemStack cancel = makeItem(Material.RED_STAINED_GLASS_PANE,
            ChatColor.RED + "✖ Bekor qilish",
            Arrays.asList(ChatColor.GRAY + "Hech narsa o'zgarmaydi"));
        inv.setItem(10, cancel);

        ItemStack info = makeItem(Material.CYAN_BED,
            ChatColor.AQUA + homeName.toUpperCase(),
            Arrays.asList(ChatColor.GRAY + "Shu homeni o'chirasizmi?"));
        inv.setItem(13, info);

        player.openInventory(inv);
        player.setMetadata("pendingDelete", new org.bukkit.metadata.FixedMetadataValue(plugin, homeName));
    }

    public static String getHomeNameByDyeSlot(int slot) {
        for (int i = 0; i < DYE_SLOTS.length; i++) {
            if (DYE_SLOTS[i] == slot) return HOME_NAMES[i];
        }
        return null;
    }

    public static String getHomeNameByBedSlot(int slot) {
        for (int i = 0; i < BED_SLOTS.length; i++) {
            if (BED_SLOTS[i] == slot) return HOME_NAMES[i];
        }
        return null;
    }

    private Home findHome(List<Home> homes, String name) {
        for (Home h : homes) if (h.getName().equalsIgnoreCase(name)) return h;
        return null;
    }

    private ItemStack makeItem(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
                  }
