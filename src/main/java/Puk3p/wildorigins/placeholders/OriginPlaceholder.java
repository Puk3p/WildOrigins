package Puk3p.wildorigins.placeholders;

import Puk3p.wildorigins.WildOrigins;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OriginPlaceholder extends PlaceholderExpansion {
    private final WildOrigins plugin;

    public OriginPlaceholder(WildOrigins plugin) {
        this.plugin = plugin;
    }

    @Override public @NotNull String getIdentifier() { return "wildorigins"; }
    @Override public @NotNull String getAuthor() { return "Puk3p"; }
    @Override public @NotNull String getVersion() {return plugin.getDescription().getVersion(); }
    @Override public boolean persist() { return true; }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "N/A";

        UUID playerUUID = player.getUniqueId();
        String origin = plugin.getConfigManager().getPlayers().getString("players." + playerUUID + ".origin", "human");

        plugin.getLogger().info("[DEBUG] Processing placeholder request: " + params + " for " + player.getName());

        if (params.equalsIgnoreCase("origin")) {
            plugin.getLogger().info("[DEBUG] Returning origin: " + origin);
            return origin;
        }

        if (params.equalsIgnoreCase("abilities")) {
            if (plugin.getConfigManager().getConfig().contains("origins." + origin + ".effects")) {
                List<Map<?, ?>> effects = plugin.getConfigManager().getConfig().getMapList("origins." + origin + ".effects");

                if (effects.isEmpty()) {
                    plugin.getLogger().info("[DEBUG] No abilities found for origin: " + origin);
                    return "None";
                }

                List<String> abilityNames = new ArrayList<>();
                for (Map<?, ?> effectMap : effects) {
                    if (effectMap.containsKey("type")) {
                        abilityNames.add(effectMap.get("type").toString());
                    }
                }

                String result = String.join(", ", abilityNames);
                plugin.getLogger().info("[DEBUG] Returning abilities: " + result);
                return result;
            } else {
                plugin.getLogger().info("[DEBUG] No abilities section found in config for: " + origin);
                return "None";
            }
        }

        return null;
    }

}
