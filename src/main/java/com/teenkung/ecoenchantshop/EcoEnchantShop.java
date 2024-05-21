package com.teenkung.ecoenchantshop;

import com.teenkung.ecoenchantshop.Commands.MainCommand;
import com.teenkung.ecoenchantshop.Commands.TabCommand;
import com.teenkung.ecoenchantshop.GUI.ConfirmGUI;
import com.teenkung.ecoenchantshop.GUI.Handlers.ConfirmHandler;
import com.teenkung.ecoenchantshop.GUI.Handlers.LevelHandler;
import com.teenkung.ecoenchantshop.GUI.Handlers.MainHandler;
import com.teenkung.ecoenchantshop.GUI.LevelGUI;
import com.teenkung.ecoenchantshop.GUI.MainGUI;
import com.teenkung.ecoenchantshop.GUI.Wrapper.ConfirmGUIWrapper;
import com.teenkung.ecoenchantshop.GUI.Wrapper.LevelGUIWrapper;
import com.teenkung.ecoenchantshop.GUI.Wrapper.MainGUIWrapper;
import com.teenkung.ecoenchantshop.Loader.ConfigLoader;
import com.teenkung.ecoenchantshop.Loader.EnchantmentPrice;
import com.teenkung.ecoenchantshop.Loader.MessageLoader;
import com.teenkung.ecoenchantshop.Loader.SoundLoader;
import com.teenkung.ecoenchantshop.Utils.HeadDatabaseHook;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class EcoEnchantShop extends JavaPlugin {

    private ConfigLoader configLoader;
    private MessageLoader messageLoader;
    private SoundLoader soundLoader;
    private EnchantmentPrice enchantmentPrice;
    private MainGUI mainGUI;
    private LevelGUI levelGUI;
    private ConfirmGUI confirmGUI;

    private HeadDatabaseAPI hdbHook;
    private boolean hasEcoEnchants;
    private boolean hasAdvancedEnchantments;
    private boolean hasHDB;

    private Economy econ = null;

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe("Could not detect Vault as dependencies, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            this.hasHDB = true;
            getLogger().info("HeadDatabase plugin detected! waiting for database load event. . .");
            Bukkit.getPluginManager().registerEvents(new HeadDatabaseHook(this), this);
        } else {
            this.hasHDB = false;
            reload();
        }
        Bukkit.getPluginManager().registerEvents(new MainHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new LevelHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ConfirmHandler(this), this);
        getCommand("ecoenchantshop").setExecutor(new MainCommand(this));
        getCommand("ecoenchantshop").setTabCompleter(new TabCommand());
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (MainGUIWrapper.isPluginGUI(inv)) {
                player.closeInventory();
            } else if (LevelGUIWrapper.isPluginGUI(inv)) {
                player.closeInventory();
            } else if (ConfirmGUIWrapper.isPluginGUI(inv)) {
                player.closeInventory();
            }
        }
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
        if (!hasHDB) { return null; }
        if (hdbHook != null) {
            return hdbHook;
        } else {
            getLogger().warning("Something went wrong! the HeadDatabaseAPI is expected to have data but is null instead!");
            return null;
        }
    }

    public void reload() {
        this.configLoader = new ConfigLoader(this);
        if (this.hasHDB) {
            this.hdbHook = new HeadDatabaseAPI();
        }
        this.mainGUI = new MainGUI(this);
        this.levelGUI = new LevelGUI(this);
        this.confirmGUI = new ConfirmGUI(this);
        this.messageLoader = new MessageLoader(this);
        this.soundLoader = new SoundLoader(this);
        this.enchantmentPrice = new EnchantmentPrice(this);
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
