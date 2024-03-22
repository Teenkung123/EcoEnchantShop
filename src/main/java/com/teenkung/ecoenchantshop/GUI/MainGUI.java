package com.teenkung.ecoenchantshop.GUI;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Holder.MainGUIHolder;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainGUI {

    private final EcoEnchantShop plugin;
    private Map<Integer, Inventory> inventory;
    private final ArrayList<Integer> freeSlot = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34));

    public MainGUI(EcoEnchantShop plugin) {
        this.plugin = plugin;

    }

    public void loadGUI() {
        int size = plugin.getEnchantmentPrice().getAvailableEnchantments().size();
        int pages = Double.valueOf(Math.ceil((double) size /21)).intValue();
        for (int i = 0; i < pages; i++) {
            Inventory inv = Bukkit.createInventory(null, 45);
            buildLayout(inv);
            MainGUIHolder.addInventory(inv);
            inventory.put(i, inv);
        }
    }

    private void buildLayout(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (!freeSlot.contains(i)) {
                inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
    }

    private void fillItems(Inventory inv, Integer page) {
        int start = page*21;
        ArrayList<EcoEnchant> enchants = new ArrayList<>(plugin.getEnchantmentPrice().getAvailableEnchantments());
        for (int i = 0 ; i < 21 ; i++) {
            EcoEnchant enchant = enchants.get(start);
            inv.setItem(start, createItemStack(enchant));
            MainGUIHolder.addInventoryItem(inv, i, enchant);
            start++;
        }
    }

    private ItemStack createItemStack(EcoEnchant enchant) {
        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = stack.getItemMeta();
        Component display = MiniMessage.miniMessage().deserialize(enchant.getRawDisplayName());
        meta.displayName(display);
        stack.setItemMeta(meta);
        return stack;
    }

}
