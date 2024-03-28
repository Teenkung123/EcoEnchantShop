package com.teenkung.ecoenchantshop.GUI.Wrapper;

import com.willfp.ecoenchants.enchant.EcoEnchant;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class MainGUIWrapper {

    private static final Map<Inventory, MainGUIData> inventories = new HashMap<>();

    public static void addInventory(Inventory inventory) {
        inventories.put(inventory, new MainGUIData(new HashMap<>()));
    }

    public static void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
    }

    public static boolean isMainGUI(Inventory inventory) {
        return inventories.containsKey(inventory);
    }

    public static void addInventoryItems(Inventory inventory, Map<Integer, EcoEnchant> items) {
        if (isMainGUI(inventory)) {
            inventories.get(inventory).items().putAll(items);
        }
    }

    public static void addInventoryItem(Inventory inventory, Integer slot, EcoEnchant enchantment) {
        if (isMainGUI(inventory)) {
            inventories.get(inventory).items().put(slot, enchantment);
        }
    }

    public static void clearMainGUIHolder() {
        inventories.clear();
    }


}
