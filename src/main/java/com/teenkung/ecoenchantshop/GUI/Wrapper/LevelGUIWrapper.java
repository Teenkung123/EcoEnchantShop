package com.teenkung.ecoenchantshop.GUI.Wrapper;

import com.willfp.ecoenchants.enchant.EcoEnchant;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@SuppressWarnings("unused")
public class LevelGUIWrapper {

    private static final HashMap<Inventory, EcoEnchant> inventories = new HashMap<>();
    private static final HashMap<Inventory, ItemStack> searches = new HashMap<>();

    public static void addInventory(Inventory inventory, EcoEnchant enchantment, ItemStack search) {
        inventories.put(inventory, enchantment);
        searches.put(inventory, search);
    }

    public static void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
        searches.remove(inventory);
    }

    public static boolean isPluginGUI(Inventory inventory) {
        return inventories.containsKey(inventory);
    }

    public static void clearGUIHolder() {
        inventories.clear();
        searches.clear();
    }
    public static EcoEnchant getEnchant(Inventory inv) {
        return inventories.getOrDefault(inv, null);
    }
    public static ItemStack getSearch(Inventory inv) {
        return searches.getOrDefault(inv, null);
    }

}
