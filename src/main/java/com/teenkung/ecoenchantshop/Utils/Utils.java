package com.teenkung.ecoenchantshop.Utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.teenkung.ecoenchantshop.EcoEnchantShop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static EcoEnchantShop plugin;

    public Utils(EcoEnchantShop plugin) {
        Utils.plugin = plugin;
    }

    public static String transformLegacyEnchantmentDescription(String message) {
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

    /**
     * Creates an ItemStack based on the configuration provided.
     * Supports custom head textures and items from the HeadDatabase.
     *
     * @param config The configuration section containing item specifications.
     * @return The constructed ItemStack or null if an error occurs.
     */
    public static ItemStack createItem(@Nullable ConfigurationSection config) {
        if (config == null) {
            return null;
        }
        // Retrieve item specifications from the configuration
        String material = config.getString("Item", "STONE");
        int amount = config.getInt("Amount", 1);
        String name = config.getString("Name", "<red>Invalid Item Name");
        ArrayList<String> lores = new ArrayList<>(config.getStringList("Lore"));
        int modelData = config.getInt("ModelData", 0);
        String headID = config.getString("HeadDatabase", "");
        String headValue = config.getString("HeadValue", ""); // Additional configuration for PLAYER_HEAD
        ItemStack item = null;

        // Check if the item is from the HeadDatabase
        if (material.equalsIgnoreCase("HeadDatabase")) {
            if (plugin.getHeadDatabaseAPI() != null) {
                try {
                    item = plugin.getHeadDatabaseAPI().getItemHead(headID);
                } catch (NullPointerException nullPointerException) {
                    plugin.getLogger().warning("HeadDatabase API returned null for headID: " + headID);
                    return null;
                }
            }
        } else if (material.equalsIgnoreCase("PLAYER_HEAD") && !headValue.isEmpty()) {
            // Create a PLAYER_HEAD item with a custom texture
            item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            PlayerProfile profile = meta.getPlayerProfile();
            if (profile != null) {
                profile.getProperties().add(new ProfileProperty("textures", headValue));
            }
            meta.setPlayerProfile(profile);
        } else {
            // Handle regular materials
            Material mat = Material.getMaterial(material);
            if (mat == null) {
                plugin.getLogger().severe("Invalid material specified: " + material);
                return null;
            }
            item = new ItemStack(mat);
        }

        if (item != null) {
            // Set item properties from the configuration
            item.setAmount(amount);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize(name));
            ArrayList<Component> newLore = new ArrayList<>();
            for (String lore : lores) {
                newLore.add(MiniMessage.miniMessage().deserialize(lore));
            }
            meta.lore(newLore);

            // Apply custom model data if applicable
            if (meta instanceof SkullMeta) {
                // Ensure player profile is properly applied to SkullMeta
                ((SkullMeta) meta).setPlayerProfile(((SkullMeta) meta).getPlayerProfile());
            } else {
                if (meta.hasCustomModelData()) {
                    meta.setCustomModelData(modelData);
                }
            }
            item.setItemMeta(meta);
        }

        return item;
    }

}
