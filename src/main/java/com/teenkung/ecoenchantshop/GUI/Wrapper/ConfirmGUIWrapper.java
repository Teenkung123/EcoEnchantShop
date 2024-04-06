package com.teenkung.ecoenchantshop.GUI.Wrapper;

import com.teenkung.ecoenchantshop.Utils.LevelHolder;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

@SuppressWarnings("unused")
public class ConfirmGUIWrapper {

    private static final HashMap<Inventory, LevelHolder> inventories = new HashMap<>();
    private static final HashMap<Inventory, Integer> levels = new HashMap<>();

    public static void addInventory(Inventory inventory, LevelHolder enchantment, Integer level) {
        inventories.put(inventory, enchantment);
        levels.put(inventory, level);
    }

    public static void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
        levels.remove(inventory);
    }

    public static boolean isPluginGUI(Inventory inventory) {
        return inventories.containsKey(inventory);
    }

    public static void clearGUIHolder() {
        inventories.clear();
        levels.clear();
    }
    public static LevelHolder getLevelHolder(Inventory inv) {
        return inventories.getOrDefault(inv, null);
    }
    public static Integer getLevel(Inventory inv) {
        return levels.getOrDefault(inv, null);
    }
}
