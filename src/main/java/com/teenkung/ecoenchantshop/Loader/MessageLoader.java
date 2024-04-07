package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageLoader {

    @SuppressWarnings("FieldCanBeLocal")
    private final EcoEnchantShop plugin;
    private final FileConfiguration config;
    private String noPermission;
    private String notEnoughMoney;
    private String boughtItem;
    private String notEnoughInventory;
    private String reloadMessage;
    private String reloadedMessage;

    public MessageLoader(EcoEnchantShop plugin) {
        this.plugin = plugin;
        plugin.saveResource("messages.yml", false);
        File messageFile = new File(plugin.getDataFolder(), "messages.yml");
        this.config = YamlConfiguration.loadConfiguration(messageFile);
        loadMessages();
    }

    private void loadMessages() {
        this.noPermission = config.getString("NoPermission", "");
        this.notEnoughMoney = config.getString("NotEnoughMoney", "");
        this.boughtItem = config.getString("BoughtItem", "");
        this.notEnoughInventory = config.getString("NotEnoughInventory", "");
        this.reloadMessage = config.getString("Reload", "");
        this.reloadedMessage = config.getString("Reloaded", "");
    }

    public String getNoPermission() {
        return noPermission;
    }

    public String getNotEnoughMoney() {
        return notEnoughMoney;
    }

    public String getBoughtItem() {
        return boughtItem;
    }

    public String getNotEnoughInventory() {
        return notEnoughInventory;
    }

    public String getReloadMessage() {
        return reloadMessage;
    }

    public String getReloadedMessage() {
        return reloadedMessage;
    }
}
