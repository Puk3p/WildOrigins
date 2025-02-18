package Puk3p.wildorigins.commands;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AdminCommands implements CommandExecutor, TabCompleter {
    private final WildOrigins plugin;

    public AdminCommands(WildOrigins plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("wildorigins.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "setorigin":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /setorigin <player> <origin>");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }

                String origin = args[1].toLowerCase();
                Set<String> availableOrigins = plugin.getConfigManager().getAbilities().getConfigurationSection("origins").getKeys(false);
                if (!availableOrigins.contains(origin)) {
                    sender.sendMessage(ChatColor.RED + "Invalid origin! Available origins: " + String.join(", ", availableOrigins));
                    return true;
                }

                UUID targetUUID = target.getUniqueId();
                plugin.getConfigManager().getPlayers().set("players." + targetUUID + ".origin", origin);
                plugin.getConfigManager().savePlayers();

                sender.sendMessage(ChatColor.GREEN + "You have set " + ChatColor.AQUA + target.getName() + "'s" + ChatColor.GREEN + " origin to " + ChatColor.GOLD + origin);
                target.sendMessage(ChatColor.AQUA + "Your origin has been set to " + ChatColor.GOLD + origin + ChatColor.AQUA + " by an admin!");
                return true;

            case "resetorigin":
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /resetorigin <player>");
                    return true;
                }

                Player resetTarget = Bukkit.getPlayer(args[0]);
                if (resetTarget == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }

                UUID resetUUID = resetTarget.getUniqueId();
                plugin.getConfigManager().getPlayers().set("players." + resetUUID + ".origin", "human");
                plugin.getConfigManager().savePlayers();

                sender.sendMessage(ChatColor.GREEN + "You have reset " + ChatColor.AQUA + resetTarget.getName() + "'s"
                    + ChatColor.GREEN + " origin to human");
                resetTarget.sendMessage(ChatColor.AQUA + "Your origin has been reset to " + ChatColor.GOLD + "human"
                    + ChatColor.AQUA + " by an admin");
                return true;

            case "checkorigin":
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /checkorigin <player>");
                    return true;
                }

                Player checkTarget = Bukkit.getPlayer(args[0]);
                if (checkTarget == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }

                UUID checkUUID = checkTarget.getUniqueId();
                String playerOrigin = plugin.getConfigManager().getPlayers().getString(
                        "players." + checkUUID + ".origin", "human");

                sender.sendMessage(ChatColor.AQUA + checkTarget.getName() + "'s origin is " + ChatColor.GOLD + playerOrigin);
                return true;

            default:
                sender.sendMessage(ChatColor.RED + "Invalid command!");
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("wildorigins.admin")) return suggestions;

        if (command.getName().equalsIgnoreCase("setorigin")) {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    suggestions.add(player.getName());
                }
            } else if (args.length == 2) {
                Set<String> availableOrigins = plugin.getConfigManager().getAbilities().getConfigurationSection("origins").getKeys(false);
                suggestions.addAll(availableOrigins);
            }
        }
        return suggestions;
    }
}
