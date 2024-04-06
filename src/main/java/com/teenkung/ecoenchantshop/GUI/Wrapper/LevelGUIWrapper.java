package com.teenkung.ecoenchantshop.GUI.Wrapper;

import com.willfp.ecoenchants.enchant.EcoEnchant;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

@SuppressWarnings("unused")
public class LevelGUIWrapper {

    private static final HashMap<Inventory, EcoEnchant> inventories = new HashMap<>();

    public static void addInventory(Inventory inventory, EcoEnchant enchantment) {
        inventories.put(inventory, enchantment);
    }

    public static void removeInventory(Inventory inventory) {

        inventories.remove(inventory);
    }

    public static boolean isPluginGUI(Inventory inventory) {
        return inventories.containsKey(inventory);
    }

    public static void clearGUIHolder() {
        inventories.clear();
    }
    public static EcoEnchant getEnchant(Inventory inv) {
        return inventories.getOrDefault(inv, null);
    }


}
