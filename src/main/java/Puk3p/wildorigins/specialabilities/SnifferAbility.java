package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class SnifferAbility implements Listener {
    private final WildOrigins plugin;
    private final Set<Material> plants = new HashSet<>(Arrays.asList(
            Material.WHEAT, Material.BEETROOTS, Material.MELON_STEM, Material.PUMPKIN_STEM,
            Material.OAK_SAPLING, Material.BIRCH_SAPLING, Material.SPRUCE_SAPLING, Material.JUNGLE_SAPLING,
            Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING, Material.MANGROVE_PROPAGULE, Material.CHERRY_SAPLING
    ));
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final CooldownBossBar bossBar;

    public SnifferAbility(WildOrigins plugin) {
        this.plugin = plugin;
        this.bossBar = new CooldownBossBar(plugin);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin", "human");

        if (!origin.equalsIgnoreCase("sniffer")) return;

        //cd check
        int cooldownTime = plugin.getConfigManager().getConfig().getInt("settings.ability-cooldown", 15);
        if (cooldowns.containsKey(playerUUID)) {
            long lastUse = cooldowns.get(playerUUID);
            if (System.currentTimeMillis() - lastUse < cooldownTime * 1000L) {
                player.sendMessage(ChatColor.RED + "Your Sniffer ability is on cooldown!");
                return;
            }
        }

        // plants nearby checking
        List<Block> foundPlants = new ArrayList<>();
        int radius = 5;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -1; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = player.getLocation().add(x, y, z).getBlock();
                    if (plants.contains(block.getType())) {
                        foundPlants.add(block);
                    }
                }
            }
        }

        //particles showing
        if (!foundPlants.isEmpty()) {
            for (Block plant : foundPlants) {
                player.spawnParticle(Particle.VILLAGER_HAPPY, plant.getLocation().add(0.5, 0.5, 0.5), 10);
            }
            player.playSound(player.getLocation(), Sound.ENTITY_SNIFFER_HAPPY, 1.0f, 1.0f);
            player.sendMessage(ChatColor.GREEN + "Your Sniffer instincts detect plants nearby!");

            // chance to get a rare seed
            if (Math.random() < 0.3) { // 30% chance
                ItemStack seed = new ItemStack(getRandomSeed(), 1);
                player.getInventory().addItem(seed);
                player.sendMessage(ChatColor.GOLD + "You found a hidden seed!");
            }
        } else {
            player.sendMessage(ChatColor.GRAY + "You sense no plants nearby...");
        }

        cooldowns.put(playerUUID, System.currentTimeMillis());
        bossBar.startCooldown(player, cooldownTime);
    }

    private Material getRandomSeed() {
        List<Material> seeds = Arrays.asList(
                Material.WHEAT_SEEDS, Material.BEETROOT_SEEDS,
                Material.MELON_SEEDS, Material.PUMPKIN_SEEDS
        );
        return seeds.get(new Random().nextInt(seeds.size()));
    }
}
