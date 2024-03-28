package com.teenkung.ecoenchantshop.GUI.Wrapper;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class MainGUIWrapper {

    private static final HashMap<Inventory, Integer> inventories = new HashMap<>();

    public static void addInventory(Inventory inventory, Integer page) {
        inventories.put(inventory, page);
    }

    public static void removeInventory(Inventory inventory) {

        inventories.remove(inventory);
    }

    public static boolean isMainGUI(Inventory inventory) {
        return inventories.containsKey(inventory);
    }

    public static void clearMainGUIHolder() {
        inventories.clear();
    }
    public static Integer getPage(Inventory inv) {
        return inventories.getOrDefault(inv, null);
    }


}
