package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.UUID;

public class ChickenAbility implements Listener {
    private final WildOrigins plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final CooldownBossBar bossBar;

    public ChickenAbility(WildOrigins plugin) {
        this.plugin = plugin;
        this.bossBar = new CooldownBossBar(plugin);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + player.getUniqueId() + ".origin", "human");

        if (!origin.equalsIgnoreCase("chicken")) return;

        if (!event.isSneaking()) return;

        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 10);
        UUID playerUUID = player.getUniqueId();

        if (cooldowns.containsKey(playerUUID)) {
            long lastUse = cooldowns.get(playerUUID);
            if (System.currentTimeMillis() - lastUse < cooldownTime * 1000L) {
                player.sendMessage(ChatColor.RED + "Egg Bomb is on cooldown!");
                return;
            }
        }

        //Launch
        Egg egg = player.launchProjectile(Egg.class);
        egg.setVelocity(player.getLocation().getDirection().multiply(1.5)); // Boost forward speed

        player.sendMessage(ChatColor.YELLOW + "You threw an Egg Bomb!");
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);

        //set cd
        cooldowns.put(playerUUID, System.currentTimeMillis());

        // start cd
        bossBar.startCooldown(player, cooldownTime);
    }

    @EventHandler
    public void onEggHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Egg)) return;

        Egg egg = (Egg) event.getEntity();
        if (!(egg.getShooter() instanceof Player)) return;

        Player player = (Player) egg.getShooter();
        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin", "human");

        if (!origin.equalsIgnoreCase("chicken")) return;

        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 10);
        if (!cooldowns.containsKey(playerUUID) || (System.currentTimeMillis() - cooldowns.get(playerUUID)) > cooldownTime * 1000L) {
            return;
        }

        egg.getWorld().createExplosion(egg.getLocation(), 0F, false, false);
        egg.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, egg.getLocation(), 5);
        egg.getWorld().playSound(egg.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);


        double radius = 3.0;
        double damage = 4.0; //2 hearts

        for (Entity entity : egg.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity && entity != player) {
                ((LivingEntity) entity).damage(damage, player);
            }
        }
    }
}