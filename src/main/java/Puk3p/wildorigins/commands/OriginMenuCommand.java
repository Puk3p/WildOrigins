package Puk3p.wildorigins.commands;

import Puk3p.wildorigins.WildOrigins;
import Puk3p.wildorigins.menus.OriginMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OriginMenuCommand implements CommandExecutor {
    private final WildOrigins plugin;
    private final OriginMenu originMenu;

    public OriginMenuCommand(WildOrigins plugin) {
        this.plugin = plugin;
        this.originMenu = new OriginMenu(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;
        boolean useDefaultMenu = plugin.getConfigManager().getConfig().getBoolean("settings.use-default-menu", true);
        if (!useDefaultMenu) {
            player.sendMessage(ChatColor.RED + "The default menu is disabled! Use your own setup.");
            return true;
        }
        originMenu.openMenu(player);
        return true;
    }
}
