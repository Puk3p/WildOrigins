package Puk3p.wildorigins;

import Puk3p.wildorigins.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WildOrigins extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        getLogger().info("WildOrigins a fost activat!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WildOrigins a fost dezactivat!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
