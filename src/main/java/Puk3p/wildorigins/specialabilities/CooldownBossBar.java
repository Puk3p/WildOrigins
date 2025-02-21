package Puk3p.wildorigins.specialabilities;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CooldownBossBar {
    private final WildOrigins plugin;
    private final HashMap<UUID, BossBar> playerBossBars = new HashMap<>();

    public CooldownBossBar(WildOrigins plugin) {
        this.plugin = plugin;
    }

    public void startCooldown(Player player, int cooldownTime) {
        UUID playerUUID = player.getUniqueId();

        //rmv bossbar if already exist
        if (playerBossBars.containsKey(playerUUID)) {
            playerBossBars.get(playerUUID).removeAll();
        }

        BossBar bossBar = Bukkit.createBossBar(
                ChatColor.AQUA + "Ability Cooldown: " + cooldownTime + "s",
                BarColor.BLUE,
                BarStyle.SOLID
        );

        bossBar.addPlayer(player);
        playerBossBars.put(playerUUID, bossBar);

        new BukkitRunnable() {
            int timeLeft = cooldownTime;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    bossBar.removeAll();
                    playerBossBars.remove(playerUUID);
                    cancel();
                    return;
                }

                bossBar.setTitle(ChatColor.AQUA + "Ability Cooldown: " + timeLeft + "s");

                double progress = (double) timeLeft / cooldownTime;
                bossBar.setProgress(progress);

                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
