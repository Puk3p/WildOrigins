package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.UUID;

public class CatAbility implements Listener {
    private final WildOrigins plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final CooldownBossBar bossBar;

    public CatAbility(WildOrigins plugin) {
        this.plugin = plugin;
        this.bossBar = new CooldownBossBar(plugin);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin", "human");

        if (!origin.equalsIgnoreCase("cat")) return;

        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 30);

        // Cooldown check
        if (cooldowns.containsKey(playerUUID)) {
            long lastUse = cooldowns.get(playerUUID);
            if (System.currentTimeMillis() - lastUse < cooldownTime * 1000L) {
                player.sendMessage(ChatColor.RED + "Agility is on cooldown!");
                return;
            }
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            double fallDistance = player.getFallDistance();

            if (fallDistance <= 5) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.GOLD + "You land perfectly on your feets like a cat!");
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);

                cooldowns.put(playerUUID, System.currentTimeMillis());
                bossBar.startCooldown(player, cooldownTime);
            } else {
                player.sendMessage(ChatColor.RED + "That was too high... even for a cat!");
            }
        }

    }
}
