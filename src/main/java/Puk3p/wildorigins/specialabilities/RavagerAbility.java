package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.util.Vector;

public class RavagerAbility implements Listener {
    private final WildOrigins plugin;
    private final CooldownBossBar bossBar;
    private final HashMap<UUID, Long> cooldowns;

    public RavagerAbility(WildOrigins plugin) {
        this.plugin = plugin;
        this.bossBar = new CooldownBossBar(plugin);
        this.cooldowns = new HashMap<>();
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin", "human");

        if (!origin.equals("ravager")) return;

        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 30);

        if (cooldowns.containsKey(playerUUID)) {
            long lastUse = cooldowns.get(playerUUID);
            if (System.currentTimeMillis() - lastUse < cooldownTime * 1000L) {
                player.sendMessage(ChatColor.RED + "Charge is on cooldown!");
                return;
            }
        }

        Vector direction = player.getLocation().getDirection().multiply(2.5);
        direction.setY(0.5); //small jmp eff
        player.setVelocity(direction);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 2, false, false, false));

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_RAVAGER_ROAR, 1.5f, 1.0f);
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        player.sendMessage(ChatColor.DARK_RED + "You charge forward!");


        List<Entity> nearbyEntities = player.getNearbyEntities(3, 1, 3);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != player) {
                ((LivingEntity) entity).damage(6.0, player); //3 h damage
                entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_HURT, 1.0f, 1.0f);
            }
        }

        //start cd
        cooldowns.put(playerUUID, System.currentTimeMillis());
        bossBar.startCooldown(player, cooldownTime);
    }
}