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

    public OriginMenuCommand(WildOrigins plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;
        new OriginMenu(plugin).openMenu(player);
        return true;
    }
}
