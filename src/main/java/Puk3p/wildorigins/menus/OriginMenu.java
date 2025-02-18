package Puk3p.wildorigins.menus;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

public class OriginMenu implements Listener {
    private final WildOrigins plugin;

    public OriginMenu(WildOrigins plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player) {
        FileConfiguration menuConfig = plugin.getConfigManager().getMenu();
        String title = ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.title", "&6Select Your Origin"));
        int size = menuConfig.getInt("menu.size", 27);

        Inventory menu = Bukkit.createInventory(null, size, title);
        Set<String> items = menuConfig.getConfigurationSection("menu.items").getKeys(false);

        for (String itemKey : items) {
            String materialName = menuConfig.getString("menu.items." + itemKey + ".material", "BARRIER");
            int slot = menuConfig.getInt("menu.items." + itemKey + ".slot", 0);
            String displayName = ChatColor.translateAlternateColorCodes('&', menuConfig.getString(
                    "menu.items." + itemKey + ".display_name", "&cUnknown Item"
            ));
            List<String> lore = menuConfig.getStringList("menu.items." + itemKey + ".lore");
            ItemStack item = new ItemStack(Material.matchMaterial(materialName));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);

            List<String> formattedLore = lore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .toList();
            meta.setLore(formattedLore);

            item.setItemMeta(meta);
            menu.setItem(slot, item);
        }
        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration menuConfig = plugin.getConfigManager().getMenu();
        String title = ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.title", "&6Select Your Origin"));

        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true); // Prevent item movement


            if (event.isRightClick() || event.isShiftClick()) {
                return; // Do nothing on right-click
            }

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            Set<String> items = menuConfig.getConfigurationSection("menu.items").getKeys(false);
            for (String itemKey : items) {
                String displayName = ChatColor.translateAlternateColorCodes('&', menuConfig.getString(
                        "menu.items." + itemKey + ".display_name", "&cUnknown Item"));

                if (displayName.equals(clickedItem.getItemMeta().getDisplayName())) {
                    plugin.getConfigManager().getPlayers().set("players." + player.getUniqueId() + ".origin", itemKey);
                    plugin.getConfigManager().savePlayers();

                    player.sendMessage(ChatColor.AQUA + "You have selected: " + ChatColor.GOLD + itemKey);
                    player.closeInventory();
                    return;
                }
            }
        }
    }


    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        FileConfiguration menuConfig = plugin.getConfigManager().getMenu();
        String title = ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.title", "&6Select Your Origin"));

        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true); // Prevent dragging items inside the menu
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (player.getOpenInventory().getTitle().contains("Select Your Origin")) {
            event.setCancelled(true); // Prevent dropping items while the menu is open
            player.sendMessage(ChatColor.RED + "You cannot drop items while selecting an origin!");
        }
    }


    private Material getMaterialForOrigin(String origin) {
        return switch (origin.toLowerCase()) {
            case "frog" -> Material.LIME_DYE;
            case "ravager" -> Material.IRON_AXE;
            case "sniffer" -> Material.SNIFFER_EGG;
            case "chicken" -> Material.FEATHER;
            case "wolf" -> Material.BONE;
            case "bee" -> Material.HONEYCOMB;
            case "cat" -> Material.TROPICAL_FISH;
            default -> Material.BARRIER;
        };
    }
}
