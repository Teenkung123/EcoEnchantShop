package com.teenkung.ecoenchantshop.GUI.Handlers;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.MainGUIWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

public class MainHandler implements Listener {

    private final EcoEnchantShop plugin;
    private final ArrayList<Integer> nextPageSlot = new ArrayList<>(Arrays.asList(43, 44));
    private final ArrayList<Integer> previousPageSlot = new ArrayList<>(Arrays.asList(36, 37));

    public MainHandler(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (MainGUIWrapper.isMainGUI(event.getInventory())) {
            MainGUIWrapper.removeInventory(event.getInventory());
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (MainGUIWrapper.isMainGUI(event.getClickedInventory())) {
                event.setCancelled(true);
                Inventory inv = event.getClickedInventory();
                if (nextPageSlot.contains(event.getSlot())) {
                    int page = MainGUIWrapper.getPage(inv);
                    int maxPage = Double.valueOf(Math.ceil(plugin.getEnchantmentPrice().getAvailableEnchantments().size() / Integer.valueOf(plugin.getConfigLoader().getFreeSlots().size()).doubleValue())).intValue()-1;
                    if (page + 1 <= maxPage) {
                        plugin.getMainGUI().openInventory((Player) event.getWhoClicked(), page + 1);
                    }
                } else if (previousPageSlot.contains(event.getSlot())) {
                    int page = MainGUIWrapper.getPage(inv);
                    if (page > 0) {
                        plugin.getMainGUI().openInventory((Player) event.getWhoClicked(), page - 1);
                    }
                }
            }
        }
    }

}
