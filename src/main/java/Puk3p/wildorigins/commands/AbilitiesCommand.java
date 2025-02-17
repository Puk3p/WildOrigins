package Puk3p.wildorigins.commands;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AbilitiesCommand implements CommandExecutor {
    private final WildOrigins plugin;

    public AbilitiesCommand(WildOrigins plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;
        String playerUUID = player.getUniqueId().toString();

        //get origin
        String origin = plugin.getConfigManager().getConfig().getString("players." + playerUUID + ".origin", "human");
        List<String> abilities = plugin.getConfigManager().getAbilities().getStringList("origins." + origin + ".abilities");

        if (abilities.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No abilities found for your origin " + origin);
            return true;
        }

        StringBuilder abilitiesMessage = new StringBuilder(ChatColor.GOLD + "Your Abilities: ");
        for (String ability : abilities) {
            abilitiesMessage.append("\n").append(ChatColor.YELLOW).append("- ").append(ChatColor.AQUA).append(ability);
        }
        player.sendMessage(abilitiesMessage.toString());
        return true;
    }
}
