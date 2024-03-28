package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ConfigLoader {

    private final EcoEnchantShop plugin;
    private final FileConfiguration config;
    private ArrayList<EcoEnchant> enchantments = new ArrayList<>();
    private final ArrayList<Integer> freeSlots = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34));
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
    public ArrayList<Integer> getFreeSlots() { return freeSlots; }
}
