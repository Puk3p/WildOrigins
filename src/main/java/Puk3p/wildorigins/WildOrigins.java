package Puk3p.wildorigins;

import Puk3p.wildorigins.commands.AdminCommands;
import Puk3p.wildorigins.commands.OriginMenuCommand;
import Puk3p.wildorigins.commands.ReloadConfigCommand;
import Puk3p.wildorigins.listeners.OriginEffectsListener;
import Puk3p.wildorigins.placeholders.OriginPlaceholder;
import Puk3p.wildorigins.specialabilities.*;
import Puk3p.wildorigins.utils.ConfigManager;
import Puk3p.wildorigins.menus.OriginMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class WildOrigins extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        AdminCommands adminCommands = new AdminCommands(this);

        registerCommands(adminCommands);
        registerEvents();

        // Suport pentru PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new OriginPlaceholder(this).register();
            getLogger().info("PlaceholderAPI detected! Registering placeholders...");
        }

        getLogger().info("WildOrigins a fost activat!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WildOrigins a fost dezactivat!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    private void registerCommand(String commandName, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = getCommand(commandName);
        if (command == null) {
            getLogger().warning("Comanda '" + commandName + "' nu a fost găsită în plugin.yml!");
            return;
        }
        command.setExecutor(executor);
        if (tabCompleter != null) {
            command.setTabCompleter(tabCompleter);
        }
    }


    private void registerCommands(AdminCommands adminCommands) {
        registerCommand("reloadconfig", new ReloadConfigCommand(this), null);
        registerCommand("abilities", adminCommands, null);
        registerCommand("originmenu", new OriginMenuCommand(this), null);
        registerCommand("setorigin", adminCommands, adminCommands);
        registerCommand("resetorigin", adminCommands, null);
        registerCommand("checkorigin", adminCommands, null);
    }


    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new FrogAbility(this), this);
        Bukkit.getPluginManager().registerEvents(new ChickenAbility(this), this);
        Bukkit.getPluginManager().registerEvents(new BeeAbility(this), this);
        Bukkit.getPluginManager().registerEvents(new SnifferAbility(this), this);
        Bukkit.getPluginManager().registerEvents(new RavagerAbility(this), this);


        OriginMenu originMenu = new OriginMenu(this);
        OriginEffectsListener originEffectsListener = new OriginEffectsListener(this);
        Bukkit.getPluginManager().registerEvents(originEffectsListener, this);
        Bukkit.getPluginManager().registerEvents(originMenu, this);
    }
}
