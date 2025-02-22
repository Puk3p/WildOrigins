package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class WolfAbility implements Listener {
    private final WildOrigins plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final CooldownBossBar bossBar;

    public WolfAbility(WildOrigins plugin) {
        this.plugin = plugin;
        this.bossBar = new CooldownBossBar(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin", "human");

        if (!origin.equalsIgnoreCase("wolf")) return;

        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 30);
        int radius = 5;


        if (cooldowns.containsKey(playerUUID)) {
            long lastUse = cooldowns.get(playerUUID);
            if (System.currentTimeMillis() - lastUse < cooldownTime * 1000L) {
                return;
            }
        }


        boolean nearbyPlayers = false;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player) && onlinePlayer.getWorld().equals(player.getWorld()) &&
                    onlinePlayer.getLocation().distanceSquared(player.getLocation()) <= (radius * radius)) {
                nearbyPlayers = true;
                break;
            }
        }

        if (nearbyPlayers) {
            if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0, false, false, false));
                player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0, 1, 0), 10);
                player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.0f, 1.0f);
                player.sendMessage(ChatColor.GOLD + "Your pack empowers you! (+20% Damage)");


                cooldowns.put(playerUUID, System.currentTimeMillis());
                bossBar.startCooldown(player, cooldownTime);
            }
        } else {
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.sendMessage(ChatColor.RED + "You are alone... Pack Tactics deactivated.");
            }
        }
    }
}
