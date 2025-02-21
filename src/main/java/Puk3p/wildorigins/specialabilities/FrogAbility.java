package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class FrogAbility implements Listener {
    private final WildOrigins plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final CooldownBossBar bossBar;

    public FrogAbility(WildOrigins plugin) {
        this.plugin = plugin;
        this.bossBar = new CooldownBossBar(plugin);
    }

    @EventHandler
    public void onPlayerSwim(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + player.getUniqueId() + ".origin", "human");

        if (!origin.equalsIgnoreCase("frog")) return;


        plugin.getLogger().info("[DEBUG] FrogAbility triggered for: " + player.getName());


        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 30);
        UUID playerUUID = player.getUniqueId();

        if (cooldowns.containsKey(playerUUID)) {
            long lastUse = cooldowns.get(playerUUID);
            if (System.currentTimeMillis() - lastUse < cooldownTime * 1000L) {
                plugin.getLogger().info("[DEBUG] Cooldown active for: " + player.getName());
                return;
            }
        }

        Material feetBlock = player.getLocation().getBlock().getType();
        Material eyeBlock = player.getEyeLocation().getBlock().getType();

        if (feetBlock == Material.WATER || eyeBlock == Material.WATER || feetBlock == Material.BUBBLE_COLUMN) {
            plugin.getLogger().info("[DEBUG] " + player.getName() + " is in water, applying effects!");

            player.setWalkSpeed(0.25f);

            player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 200, 0, false, false, false));

            if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                plugin.getLogger().info("[DEBUG] Successfully applied Dolphin's Grace to " + player.getName());
            } else {
                plugin.getLogger().warning("[DEBUG] Failed to apply Dolphin's Grace to " + player.getName());
            }

            player.getWorld().spawnParticle(Particle.WATER_BUBBLE, player.getLocation(), 10);
            player.playSound(player.getLocation(), Sound.ENTITY_TROPICAL_FISH_FLOP, 1.0f, 1.5f);

            cooldowns.put(playerUUID, System.currentTimeMillis());
            bossBar.startCooldown(player, cooldownTime);

            player.sendMessage(ChatColor.AQUA + "You feel the power of the frog! (Cooldown: " + cooldownTime + "s)");

        } else {
            plugin.getLogger().info("[DEBUG] " + player.getName() + " left water, resetting speed.");
            player.setWalkSpeed(0.2f); //nrml speed
        }
    }
}
