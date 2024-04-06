package com.teenkung.ecoenchantshop;

import com.teenkung.ecoenchantshop.Commands.MainCommand;
import com.teenkung.ecoenchantshop.GUI.ConfirmGUI;
import com.teenkung.ecoenchantshop.GUI.Handlers.ConfirmHandler;
import com.teenkung.ecoenchantshop.GUI.Handlers.LevelHandler;
import com.teenkung.ecoenchantshop.GUI.Handlers.MainHandler;
import com.teenkung.ecoenchantshop.GUI.LevelGUI;
import com.teenkung.ecoenchantshop.GUI.MainGUI;
import com.teenkung.ecoenchantshop.Loader.ConfigLoader;
import com.teenkung.ecoenchantshop.Loader.EnchantmentPrice;
import com.teenkung.ecoenchantshop.Loader.MessageLoader;
import com.teenkung.ecoenchantshop.Loader.SoundLoader;
import com.teenkung.ecoenchantshop.Utils.HeadDatabaseHook;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EcoEnchantShop extends JavaPlugin {

    private ConfigLoader configLoader;
    private MessageLoader messageLoader;
    private SoundLoader soundLoader;
    private EnchantmentPrice enchantmentPrice;
    private MainGUI mainGUI;
    private LevelGUI levelGUI;
    private ConfirmGUI confirmGUI;
    private boolean useHDB;
    private HeadDatabaseAPI hdbHook;
    private Economy econ = null;

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe("Could not setup economy!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            this.useHDB = true;
            getLogger().info("HeadDatabase plugin detected! waiting for database load event. . .");
            Bukkit.getPluginManager().registerEvents(new HeadDatabaseHook(this), this);
        } else {
            this.useHDB = false;
            reload();
        }
        Bukkit.getPluginManager().registerEvents(new MainHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new LevelHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ConfirmHandler(this), this);
        Objects.requireNonNull(getCommand("ecoenchantshop")).setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigLoader getConfigLoader() { return configLoader; }
    public EnchantmentPrice getEnchantmentPrice() { return enchantmentPrice; }
    public MainGUI getMainGUI() { return mainGUI; }
    public LevelGUI getLevelGUI() { return levelGUI; }
    public ConfirmGUI getConfirmGUI() { return confirmGUI; }
    public SoundLoader getSoundLoader() { return soundLoader; }
    public MessageLoader getMessageLoader() { return messageLoader; }
    public Economy getEconomy() { return econ; }
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
        this.messageLoader = new MessageLoader(this);
        this.soundLoader = new SoundLoader(this);
        this.enchantmentPrice = new EnchantmentPrice(this);
        this.mainGUI = new MainGUI(this);
        this.levelGUI = new LevelGUI(this);
        this.confirmGUI = new ConfirmGUI(this);
        if (this.useHDB) {
            this.hdbHook = new HeadDatabaseAPI();
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        //noinspection ConstantValue
        return econ != null;
    }


}
