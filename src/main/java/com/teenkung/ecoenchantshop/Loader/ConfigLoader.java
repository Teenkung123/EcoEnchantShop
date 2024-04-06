package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.ConfirmMenuConfig;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.LevelMenuConfig;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.MainMenuConfig;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;

public class ConfigLoader {

    private final EcoEnchantShop plugin;
    private ArrayList<EcoEnchant> enchantments = new ArrayList<>();
    private MainMenuConfig mainMenu;
    private LevelMenuConfig levelMenu;
    private ConfirmMenuConfig confirmMenu;
    private EnchantItemTemplate enchantTemplate;

    public ConfigLoader(EcoEnchantShop plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveDefaultConfig();
        plugin.getLogger().info("Loading configuration. . .");
        loadConfigs();
        loadEnchantments();
        plugin.getLogger().info("Configuration loaded!");
    }

    public void loadConfigs() {
        // Load each menu using the new generic method
        mainMenu = loadMenuConfig("Menu.Main", MainMenuConfig.class);
        levelMenu = loadMenuConfig("Menu.Level", LevelMenuConfig.class);
        confirmMenu = loadMenuConfig("Menu.Confirm", ConfirmMenuConfig.class);
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("ItemTemplate.Enchantment");
        if (section != null) {
            enchantTemplate = new EnchantItemTemplate(section);
        }
    }

    private <T> T loadMenuConfig(String path, Class<T> clazz) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path);
        if (section == null) {
            plugin.getLogger().severe("Could not load " + path + " from configuration!");
            return null;
        }

        try {
            // Assuming each menu config class has a constructor that takes (EcoEnchantShop, ConfigurationSection)
            Constructor<T> constructor = clazz.getConstructor(EcoEnchantShop.class, ConfigurationSection.class);
            return constructor.newInstance(plugin, section);
        } catch (Exception e) {
            plugin.getLogger().severe("Error instantiating " + clazz.getSimpleName() + ": " + e.getMessage());
            return null;
        }
    }

    public void loadEnchantments() {
        enchantments = new ArrayList<>(EcoEnchants.INSTANCE.values());
        enchantments.sort(Comparator.comparing(enchant -> enchant.getEnchantmentKey().getKey()));
    }

    protected ArrayList<EcoEnchant> getEnchantments() { return enchantments; }
    public MainMenuConfig getMainMenuConfig() { return mainMenu; }
    public EnchantItemTemplate getEnchantTemplate() { return enchantTemplate; }

    public LevelMenuConfig getLevelMenuConfig() { return levelMenu; }
    public ConfirmMenuConfig getConfirmMenuConfig() { return confirmMenu; }
}
