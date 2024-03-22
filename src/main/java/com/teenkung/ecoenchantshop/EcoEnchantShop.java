package com.teenkung.ecoenchantshop;

import com.teenkung.ecoenchantshop.Loader.ConfigLoader;
import com.teenkung.ecoenchantshop.Loader.EnchantmentPrice;
import org.bukkit.plugin.java.JavaPlugin;

public final class EcoEnchantShop extends JavaPlugin {

    private ConfigLoader configLoader;
    private EnchantmentPrice enchantmentPrice;

    @Override
    public void onEnable() {
        this.configLoader = new ConfigLoader(this);
        this.enchantmentPrice = new EnchantmentPrice(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigLoader getConfigLoader() { return configLoader; }
    public EnchantmentPrice getEnchantmentPrice() { return enchantmentPrice; }
}
