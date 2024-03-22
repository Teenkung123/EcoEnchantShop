package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Comparator;

public class ConfigLoader {

    public EcoEnchantShop plugin;
    public FileConfiguration config;
    public ArrayList<EcoEnchant> enchantments = new ArrayList<>();

    public ConfigLoader(EcoEnchantShop plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveDefaultConfig();

        loadEnchantments();
    }

    public void loadEnchantments() {
        enchantments = new ArrayList<>(EcoEnchants.INSTANCE.values());
        enchantments.sort(Comparator.comparing(enchant -> enchant.getEnchantmentKey().getKey()));
    }

    public ArrayList<EcoEnchant> getEnchantments() { return enchantments; }
}
