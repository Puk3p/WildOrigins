package Puk3p.wildorigins;

import Puk3p.wildorigins.commands.AbilitiesCommand;
import Puk3p.wildorigins.commands.AdminCommands;
import Puk3p.wildorigins.commands.OriginMenuCommand;
import Puk3p.wildorigins.commands.ReloadConfigCommand;
import Puk3p.wildorigins.listeners.OriginEffectsListener;
import Puk3p.wildorigins.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import Puk3p.wildorigins.menus.OriginMenu;

public class WildOrigins extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        AdminCommands adminCommands = new AdminCommands(this);

        getCommand("reloadconfig").setExecutor(adminCommands);
        getCommand("abilities").setExecutor(adminCommands);
        getCommand("originmenu").setExecutor(new OriginMenuCommand(this));

        getCommand("setorigin").setExecutor(adminCommands);
        getCommand("setorigin").setTabCompleter(adminCommands);

        getCommand("resetorigin").setExecutor(adminCommands);
        getCommand("checkorigin").setExecutor(adminCommands);

        OriginMenu originMenu = new OriginMenu(this);
        Bukkit.getPluginManager().registerEvents(new OriginEffectsListener(this), this);
        Bukkit.getPluginManager().registerEvents(originMenu, this);

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
