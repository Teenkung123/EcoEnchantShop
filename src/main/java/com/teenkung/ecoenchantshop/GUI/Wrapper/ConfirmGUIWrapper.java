package com.teenkung.ecoenchantshop.GUI.Wrapper;

import com.teenkung.ecoenchantshop.Utils.LevelHolder;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

@SuppressWarnings("unused")
public class ConfirmGUIWrapper {

    private static final HashMap<Inventory, LevelHolder> inventories = new HashMap<>();

    public static void addInventory(Inventory inventory, LevelHolder enchantment) {
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
    public static LevelHolder getPage(Inventory inv) {
        return inventories.getOrDefault(inv, null);
    }

}
