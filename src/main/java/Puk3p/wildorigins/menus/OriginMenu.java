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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class OriginMenu implements Listener {
    private final WildOrigins plugin;

    public OriginMenu(WildOrigins plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Select Your Origin");
        FileConfiguration abilitiesConfig = plugin.getConfigManager().getAbilities();

        int slot = 10;
        for (String originKey : abilitiesConfig.getConfigurationSection("origins").getKeys(false)) {
            String originName = abilitiesConfig.getString("origins." + originKey + ".name");
            List<String> abilities = abilitiesConfig.getStringList("origins." + originKey + ".abilities");

            ItemStack item = new ItemStack(getMaterialForOrigin(originKey));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + originName);

            //abilities as item lore
            List<String> lore = abilities.stream()
                    .map(ability -> ChatColor.YELLOW + "- " + ability)
                    .toList();
            meta.setLore(lore);
            item.setItemMeta(meta);

            menu.setItem(slot, item);
            ++slot;
        }
        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Select Your Origin")) {
            event.setCancelled(true); //move item prevent

            if (event.getClickedInventory() == null || event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String selectedOrigin = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            //saving players to config
            plugin.getConfigManager().getConfig().set("players." + player.getUniqueId() + ".origin", selectedOrigin.toLowerCase());
            plugin.getConfigManager().saveConfig();

            player.sendMessage(ChatColor.GOLD + "You selected " + selectedOrigin + "!");
            player.closeInventory();
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
