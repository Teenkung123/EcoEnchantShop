package com.teenkung.ecoenchantshop;

import com.teenkung.ecoenchantshop.Commands.MainCommand;
import com.teenkung.ecoenchantshop.GUI.Handlers.MainHandler;
import com.teenkung.ecoenchantshop.GUI.MainGUI;
import com.teenkung.ecoenchantshop.Loader.ConfigLoader;
import com.teenkung.ecoenchantshop.Loader.EnchantmentPrice;
import com.teenkung.ecoenchantshop.Utils.HeadDatabaseHook;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class EcoEnchantShop extends JavaPlugin {

    private ConfigLoader configLoader;
    private EnchantmentPrice enchantmentPrice;
    private MainGUI mainGUI;
    private boolean useHDB;
    private HeadDatabaseAPI hdbHook;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            this.useHDB = true;
            getLogger().info("HeadDatabase plugin detected! waiting for database load event. . .");
            Bukkit.getPluginManager().registerEvents(new HeadDatabaseHook(this), this);
        } else {
            this.useHDB = false;
            reload();
        }
        Bukkit.getPluginManager().registerEvents(new MainHandler(this), this);
        getCommand("ecoenchantshop").setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigLoader getConfigLoader() { return configLoader; }
    public EnchantmentPrice getEnchantmentPrice() { return enchantmentPrice; }
    public MainGUI getMainGUI() { return mainGUI; }
    public HeadDatabaseAPI getHeadDatabaseAPI() {
        if (!useHDB) { return null; }
        if (hdbHook != null) {
            return hdbHook;
        } else {
            getLogger().warning("Something went wrong! the HeadDatabaseAPI is expected to have data but is null instead!");
            return null;
        }
    }

    public void reload() {
        this.configLoader = new ConfigLoader(this);
        this.enchantmentPrice = new EnchantmentPrice(this);
        this.mainGUI = new MainGUI(this);
        if (this.useHDB) {
            this.hdbHook = new HeadDatabaseAPI();
        }
    }




}
