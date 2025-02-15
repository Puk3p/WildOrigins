package Puk3p.wildorigins;

import org.bukkit.plugin.java.JavaPlugin;

public class WildOrigins extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("WildOrigins a fost activat!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WildOrigins a fost dezactivat!");
    }
}
