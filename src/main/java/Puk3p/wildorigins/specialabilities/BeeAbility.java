package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BeeAbility implements Listener {
    private final WildOrigins plugin;
    private final Set<Material> flowers = new HashSet<>(Arrays.asList(
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM,
            Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP,
            Material.PINK_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY,
            Material.WITHER_ROSE, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY
    ));

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final HashSet<UUID> buffedPlayers = new HashSet<>();
    private final CooldownBossBar bossBar;

    public BeeAbility(WildOrigins plugin) {
        this.plugin = plugin;
        this.bossBar = new CooldownBossBar(plugin);
    }

    @EventHandler
    public void onMoveNearFlowers(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin", "human");

        if (!origin.equalsIgnoreCase("bee")) return;

        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 10);

        if (cooldowns.containsKey(playerUUID)) {
            long lastUse = cooldowns.get(playerUUID);
            if (System.currentTimeMillis() - lastUse < cooldownTime * 1000L) {
                return;
            }
        }

        boolean nearFlower = isNearFlower(player);

        if (nearFlower) {
            if (!buffedPlayers.contains(playerUUID)) {
                buffedPlayers.add(playerUUID);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0, false, false, false));
                player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5);
                player.playSound(player.getLocation(), Sound.ENTITY_BEE_POLLINATE, 1.0f, 1.0f);
                player.sendMessage(ChatColor.GOLD + "You feel energized by the flowers!");


                cooldowns.put(playerUUID, System.currentTimeMillis());
                bossBar.startCooldown(player, cooldownTime);
            }
        } else {
            if (buffedPlayers.contains(playerUUID)) {
                buffedPlayers.remove(playerUUID);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.sendMessage(ChatColor.RED + "You moved away from the flowers.");
            }
        }
    }

    private boolean isNearFlower(Player player) {
        int radius = 2;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = player.getLocation().add(x, y, z).getBlock();
                    if (flowers.contains(block.getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
