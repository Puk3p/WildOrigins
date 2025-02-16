package Puk3p.wildorigins.commands;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfigCommand implements CommandExecutor {
    private final WildOrigins plugin;

    public ReloadConfigCommand(WildOrigins plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission("wildorigins.admin")) {
            plugin.getConfigManager().reloadFiles();
            String reloadMessage = plugin.getConfigManager().getMessages().getString("messages.config-reloaded", "&a[WildOrigins] Configuration reloaded!");
            return true;
        }
        String noPermissionMessage = plugin.getConfigManager().getMessages().getString("messages.no-permission", "&cYou don't have permission to use this command!");
        sender.sendMessage(noPermissionMessage.replace("&", "§"));
        return true;
    }
}
