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

        // rmv existing potion eff
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        //eff from cfg
        ConfigurationSection originConfig  = plugin.getConfigManager().getConfig().getConfigurationSection("origins." + origin);
        if (originConfig == null) return;

        List<?> effects = originConfig.getList("effects");
        if (effects == null) return;

        for (Object effectObj : effects) {
            if (!(effectObj instanceof ConfigurationSection effectSection)) continue;

            String type = effectSection.getString("type");
            int amplifier = effectSection.getInt("amplifier", 0);
            int duration = effectSection.getInt("duration", 999999); //999999 = inf

            PotionEffectType potionType = PotionEffectType.getByName(type.toUpperCase());
            if (potionType != null) {
                player.addPotionEffect(new PotionEffect(potionType, duration, amplifier, false, false));
            }
        }
    }
}
