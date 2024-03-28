package com.teenkung.ecoenchantshop;

import com.teenkung.ecoenchantshop.Commands.MainCommand;
import com.teenkung.ecoenchantshop.GUI.Handlers.MainHandler;
import com.teenkung.ecoenchantshop.GUI.MainGUI;
import com.teenkung.ecoenchantshop.Loader.ConfigLoader;
import com.teenkung.ecoenchantshop.Loader.EnchantmentPrice;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class EcoEnchantShop extends JavaPlugin {

    private ConfigLoader configLoader;
    private EnchantmentPrice enchantmentPrice;
    private MainGUI mainGUI;

    @Override
    public void onEnable() {
        this.configLoader = new ConfigLoader(this);
        this.enchantmentPrice = new EnchantmentPrice(this);
        this.mainGUI = new MainGUI(this);
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

    public static String transformColorCodesToMiniMessage(String message) {
        // Color codes mapping
        Map<String, String> colorCodes = new HashMap<>();
        colorCodes.put("&0", "<black>");
        colorCodes.put("&1", "<dark_blue>");
        colorCodes.put("&2", "<dark_green>");
        colorCodes.put("&3", "<dark_aqua>");
        colorCodes.put("&4", "<dark_red>");
        colorCodes.put("&5", "<dark_purple>");
        colorCodes.put("&6", "<gold>");
        colorCodes.put("&7", "<gray>");
        colorCodes.put("&8", "<dark_gray>");
        colorCodes.put("&9", "<blue>");
        colorCodes.put("&a", "<green>");
        colorCodes.put("&b", "<aqua>");
        colorCodes.put("&c", "<red>");
        colorCodes.put("&d", "<light_purple>");
        colorCodes.put("&e", "<yellow>");
        colorCodes.put("&f", "<white>");

        // Format codes mapping
        Map<String, String> formatCodes = new HashMap<>();
        formatCodes.put("&k", "<obfuscated>");
        formatCodes.put("&l", "<bold>");
        formatCodes.put("&m", "<strikethrough>");
        formatCodes.put("&n", "<underline>");
        formatCodes.put("&o", "<italic>");
        formatCodes.put("&r", "<dark_gray>");

        // Replace all color and format codes with their MiniMessage equivalents
        for (Map.Entry<String, String> entry : colorCodes.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : formatCodes.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }


}
