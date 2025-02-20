package Puk3p.wildorigins.listeners;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OriginEffectsListener implements Listener {
    private final WildOrigins plugin;

    public OriginEffectsListener(WildOrigins plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        applyEffects(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        applyEffects(player);
    }

    public void applyEffects(Player player) {
        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin",
                plugin.getConfigManager().getConfig().getString("settings.default-origin", "human"));

        plugin.getLogger().info("[DEBUG] Applying effects for " + player.getName() + " (Origin: " + origin + ")");


        // rmv existing potion eff
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        //eff from cfg
        ConfigurationSection originConfig  = plugin.getConfigManager().getConfig().getConfigurationSection("origins." + origin);
        if (originConfig == null) {
            plugin.getLogger().warning("[DEBUG] No effects found for origin: " + origin);
            return;
        }

        List<Map<?, ?>> effects = originConfig.getMapList("effects"); //map from list
        if (effects == null || effects.isEmpty()) {
            plugin.getLogger().warning("[DEBUG] Effects list is missing or empty for origin: " + origin);
            return;
        }


        for (Map<?, ?> effectMap : effects) {
            String type = (String) effectMap.get("type");
            int amplifier = (effectMap.containsKey("amplifier")) ? (int) effectMap.get("amplifier") : 0;
            int duration = (effectMap.containsKey("duration")) ? (int) effectMap.get("duration") : 999999;

            if (type == null) {
                plugin.getLogger().warning("[DEBUG] Missing 'type' field in effects for " + origin);
                continue;
            }

            PotionEffectType potionType = PotionEffectType.getByName(type.toUpperCase());
            if (potionType != null) {
                player.addPotionEffect(new PotionEffect(potionType, duration, amplifier, false, false));
                plugin.getLogger().info("[DEBUG] Applied effect: " + type + " (Amplifier: " + amplifier + ", Duration: " + duration + ")");
            } else {
                plugin.getLogger().warning("[DEBUG] Invalid potion effect type: " + type);
            }
        }
    }
}
