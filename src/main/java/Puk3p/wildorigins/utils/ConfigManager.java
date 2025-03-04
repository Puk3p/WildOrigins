package Puk3p.wildorigins.utils;

import Puk3p.wildorigins.WildOrigins;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {
    private final WildOrigins plugin;
    private FileConfiguration config, messages, abilities, players, menu;
    private File configFile, messagesFile, abilitiesFile, playersFile, menuFile;

    public ConfigManager(WildOrigins plugin) {
        this.plugin = plugin;
        loadFiles();
    }

    private void loadFiles() {
        plugin.getLogger().info("Loading configuration files...");
        //loading each config file
        configFile = new File(plugin.getDataFolder(), "config.yml");
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        abilitiesFile = new File(plugin.getDataFolder(), "abilities.yml");
        playersFile = new File(plugin.getDataFolder(), "players.yml");
        menuFile = new File(plugin.getDataFolder(), "menu.yml");

        //create if not exist
        if (!configFile.exists()) plugin.saveResource("config.yml", false);
        if (!messagesFile.exists()) plugin.saveResource("messages.yml", false);
        if (!abilitiesFile.exists()) plugin.saveResource("abilities.yml", false);
        if (!playersFile.exists()) plugin.saveResource("players.yml", false);
        if (!menuFile.exists()) plugin.saveResource("menu.yml", false);

        //load it
        config = YamlConfiguration.loadConfiguration(configFile);
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        abilities = YamlConfiguration.loadConfiguration(abilitiesFile);
        players = YamlConfiguration.loadConfiguration(playersFile);
        menu = YamlConfiguration.loadConfiguration(menuFile);

        plugin.getLogger().info("Messages file loaded: " + messagesFile.exists());
    }

    public FileConfiguration getConfig() { return config; }
    public FileConfiguration getMessages() { return messages; }
    public FileConfiguration getAbilities() { return abilities; }
    public FileConfiguration getPlayers() { return players; }
    public FileConfiguration getMenu() { return menu; }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config", e);
        }
    }

    public void saveMessages() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save messages", e);
        }
    }

    public void saveAbilities() {
        try {
            abilities.save(abilitiesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save abilities", e);
        }
    }

    public void savePlayers() {
        try {
            players.save(playersFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save players", e);
        }
    }

    public void saveMenu() {
        try {
            menu.save(menuFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save menu", e);
        }
    }
    public void reloadFiles() {
        loadFiles();
        plugin.getLogger().info("Configuration files have been reloaded.");
        plugin.getLogger().info("config-reloaded message: " + messages.getString("messages.config-reloaded"));
    }
}
