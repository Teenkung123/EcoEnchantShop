package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ConfigLoader {

    private final EcoEnchantShop plugin;
    private final FileConfiguration config;
    private ArrayList<EcoEnchant> enchantments = new ArrayList<>();
    private MainMenuConfig mainMenu;

    public ConfigLoader(EcoEnchantShop plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveDefaultConfig();
        plugin.getLogger().info("Loading configuration. . .");
        loadConfigs();
        loadEnchantments();
        plugin.getLogger().info("Configuration loaded!");
    }

    public void loadConfigs() {
        ConfigurationSection mainMenuSection = config.getConfigurationSection("Menu.Main");
        if (mainMenuSection != null) {
            this.mainMenu = new MainMenuConfig(plugin, mainMenuSection);
        } else {
            plugin.getLogger().severe("Could not load Main Menu GUI from configuration!");
        }
    }

    public void loadEnchantments() {
        enchantments = new ArrayList<>(EcoEnchants.INSTANCE.values());
        enchantments.sort(Comparator.comparing(enchant -> enchant.getEnchantmentKey().getKey()));
    }

    public ArrayList<EcoEnchant> getEnchantments() { return enchantments; }
    public MainMenuConfig getMainMenuConfig() { return mainMenu; }
}
