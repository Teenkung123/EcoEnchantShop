package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.Utils.LevelHolder;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnchantmentPrice {

    private final EcoEnchantShop plugin;
    private final FileConfiguration config;
    private Map<EcoEnchant, LevelHolder> enchantments;

    /**
     * Constructor for EnchantmentPrice.
     * Initializes the plugin, config, and loads enchantments.
     *
     * @param plugin The main plugin instance.
     */
    public EnchantmentPrice(EcoEnchantShop plugin) {
        this.plugin = plugin;
        File enchantmentsFile = new File(plugin.getDataFolder(), "enchantments.yml");
        this.config = YamlConfiguration.loadConfiguration(enchantmentsFile);
        if (!enchantmentsFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            enchantmentsFile.getParentFile().mkdirs();
            initEnchantmentsConfig(enchantmentsFile);
        } else {
            loadEnchantments();
        }
    }

    /**
     * Initializes the enchantments configuration with default values.
     * This includes creating sections for each enchantment and setting
     * default prices and enabled status.
     *
     * @param enchantmentsFile The file where the enchantments configuration is saved.
     */
    private void initEnchantmentsConfig(File enchantmentsFile) {
        for (EcoEnchant enchant : plugin.getConfigLoader().getEnchantments()) {
            String id = enchant.getEnchantmentKey().getKey();
            config.createSection(id);
            config.set(id + ".enabled", false);
            for (int i = 1; i <= enchant.getMaximumLevel(); i++) {
                config.set(id + "." + i, 1000 * i);
            }
        }
        saveConfig(enchantmentsFile);
        loadEnchantments();
    }

    /**
     * Saves the current state of the configuration to the file.
     *
     * @param file The file to save the configuration to.
     */
    private void saveConfig(File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save enchantments.yml, something went wrong!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads enchantments from the configuration file into the plugin.
     * This involves reading the config, checking each enchantment's enabled
     * status, and loading its levels and prices.
     */
    public void loadEnchantments() {
        plugin.getLogger().info("Loading all enchantment prices...");
        ConfigurationSection section = config.getConfigurationSection("");
        enchantments = new HashMap<>();
        if (section != null) {
            loadSectionEnchantments(section);
        } else {
            plugin.getLogger().warning("Could not find any configuration section!");
        }
    }

    /**
     * Helper method to load enchantments from a given configuration section.
     * This iterates over all enchantments defined in the config, checks if they
     * are enabled, and then loads their price levels.
     *
     * @param section The configuration section from which to load the enchantments.
     */
    private void loadSectionEnchantments(ConfigurationSection section) {
        int amount = 0;
        for (String key : section.getKeys(false)) {
            EcoEnchant enchant = EcoEnchants.INSTANCE.getByID(key);
            if (enchant != null && config.getBoolean(key + ".enabled")) {
                LevelHolder holder = new LevelHolder(enchant);
                ConfigurationSection section2 = config.getConfigurationSection(key);
                if (section2 != null) {
                    loadEnchantmentLevels(section2, holder, key);
                    enchantments.put(enchant, holder);
                    amount++;
                }
            }
        }
        plugin.getLogger().info("Loaded all " + amount + " Enchantments");
    }

    /**
     * Helper method to load enchantment levels and prices from a configuration section.
     * This reads the specific price for each level of the enchantment and stores it.
     *
     * @param section The configuration section for the specific enchantment.
     * @param holder  The LevelHolder instance to store the level and price information.
     * @param key     The key of the enchantment being processed.
     */
    private void loadEnchantmentLevels(ConfigurationSection section, LevelHolder holder, String key) {
        StringBuilder log = new StringBuilder("Loaded enchantment " + key + " levels: ");
        for (String levelKey : section.getKeys(false)) {
            if (!"enabled".equalsIgnoreCase(levelKey)) {
                try {
                    int level = Integer.parseInt(levelKey);
                    double price = section.getDouble(levelKey);
                    holder.addLevel(level, price);
                    log.append(level).append(" ");
                } catch (NumberFormatException ignored) {
                    // Ignore any entries that are not valid level numbers
                }
            }
        }
        plugin.getLogger().info(log.toString());
    }

    /**
     * Method to get specific LevelHolder of specific enchantment
     *
     * @param enchantment target EcoEnchant enchantment
     * @return            LevelHolder the LevelHolder class of target enchantment
     */
    public LevelHolder getHolders(EcoEnchant enchantment) {
        return enchantments.getOrDefault(enchantment, null);
    }

    public Set<EcoEnchant> getAvailableEnchantments() { return enchantments.keySet(); }
}
