package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.ConfirmMenuConfig;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.LevelMenuConfig;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.MainMenuConfig;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Comparator;

public class ConfigLoader {

    private MainMenuConfig mainMenu;
    private final ArrayList<EcoEnchant> enchantments;
    private LevelMenuConfig levelMenu;
    private ConfirmMenuConfig confirmMenu;
    private EnchantItemTemplate enchantTemplate;
    private boolean enchantmentSlotsIntegration;

    public ConfigLoader(EcoEnchantShop plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        plugin.getLogger().info("Loading configuration. . .");
        this.enchantmentSlotsIntegration = config.getBoolean("Options.Integrations.EnchantmentSlots", false);
        if (!Bukkit.getPluginManager().isPluginEnabled("EnchantmentSlots") && this.enchantmentSlotsIntegration) {
            plugin.getLogger().warning("The Integration for EnchantmentSlots is enabled. but couldn't find the plugin itself. Disabling the Integration.");
            this.enchantmentSlotsIntegration = false;
        }
        ConfigurationSection section = config.getConfigurationSection("ItemTemplate.Enchantment");
        if (section != null) {
            enchantTemplate = new EnchantItemTemplate(section);
            plugin.getLogger().info("enchantmentTemplate loaded.");
        }
        ConfigurationSection mainSection = config.getConfigurationSection("Menu.Main");
        if (mainSection != null) {
            mainMenu = new MainMenuConfig(plugin, mainSection);
            plugin.getLogger().info("mainMenu loaded.");
        }
        ConfigurationSection levelSection = config.getConfigurationSection("Menu.Level");
        if (levelSection != null) {
            levelMenu = new LevelMenuConfig(plugin, levelSection);
            plugin.getLogger().info("levelMenu loaded.");
        }
        ConfigurationSection confirmSection = config.getConfigurationSection("Menu.Confirm");
        if (confirmSection != null) {
            confirmMenu = new ConfirmMenuConfig(plugin, confirmSection);
            plugin.getLogger().info("confirmMenu loaded.");
        }
        enchantments = new ArrayList<>(EcoEnchants.INSTANCE.values());
        enchantments.sort(Comparator.comparing(enchant -> enchant.getEnchantmentKey().getKey()));
        plugin.getLogger().info("Configuration loaded!");
    }

    public ArrayList<EcoEnchant> getEnchantments() { return enchantments; }
    public MainMenuConfig getMainMenuConfig() { return mainMenu; }
    public EnchantItemTemplate getEnchantTemplate() { return enchantTemplate; }
    public boolean getEnchantmentSlotsIntegration() { return enchantmentSlotsIntegration; }

    public LevelMenuConfig getLevelMenuConfig() { return levelMenu; }
    public ConfirmMenuConfig getConfirmMenuConfig() { return confirmMenu; }
}
