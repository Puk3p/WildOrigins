package Puk3p.wildorigins;

import Puk3p.wildorigins.commands.AbilitiesCommand;
import Puk3p.wildorigins.commands.AdminCommands;
import Puk3p.wildorigins.commands.OriginMenuCommand;
import Puk3p.wildorigins.commands.ReloadConfigCommand;
import Puk3p.wildorigins.listeners.OriginEffectsListener;
import Puk3p.wildorigins.placeholders.OriginPlaceholder;
import Puk3p.wildorigins.specialabilities.BeeAbility;
import Puk3p.wildorigins.specialabilities.ChickenAbility;
import Puk3p.wildorigins.specialabilities.FrogAbility;
import Puk3p.wildorigins.specialabilities.SnifferAbility;
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
        Bukkit.getPluginManager().registerEvents(new FrogAbility(this), this);
        Bukkit.getPluginManager().registerEvents(new ChickenAbility(this), this);
        Bukkit.getPluginManager().registerEvents(new BeeAbility(this), this);
        Bukkit.getPluginManager().registerEvents(new SnifferAbility(this), this);

        // PAPI suport
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new OriginPlaceholder(this).register();
            getLogger().info("PlaceholderAPI detected! Registering placeholders...");
        }

        getCommand("reloadconfig").setExecutor(new ReloadConfigCommand(this));
        getCommand("abilities").setExecutor(adminCommands);
        getCommand("originmenu").setExecutor(new OriginMenuCommand(this));

        getCommand("setorigin").setExecutor(adminCommands);
        getCommand("setorigin").setTabCompleter(adminCommands);

        getCommand("resetorigin").setExecutor(adminCommands);
        getCommand("checkorigin").setExecutor(adminCommands);

        OriginMenu originMenu = new OriginMenu(this);
        OriginEffectsListener originEffectsListener = new OriginEffectsListener(this);
        Bukkit.getPluginManager().registerEvents(originEffectsListener, this);
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
