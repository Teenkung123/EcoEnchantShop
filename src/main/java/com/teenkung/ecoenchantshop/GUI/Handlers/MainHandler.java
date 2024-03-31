package com.teenkung.ecoenchantshop.GUI.Handlers;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.MainGUIWrapper;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class MainHandler implements Listener {

    private final EcoEnchantShop plugin;

    public MainHandler(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (MainGUIWrapper.isPluginGUI(event.getInventory())) {
            MainGUIWrapper.removeInventory(event.getInventory());
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (MainGUIWrapper.isPluginGUI(event.getClickedInventory())) {
                event.setCancelled(true);
                Inventory inv = event.getClickedInventory();
                if (plugin.getConfigLoader().getMainMenuConfig().getNextPageSlots().contains(event.getSlot())) {
                    int page = MainGUIWrapper.getPage(inv);
                    int maxPage = Double.valueOf(Math.ceil(plugin.getEnchantmentPrice().getAvailableEnchantments().size() / Integer.valueOf(plugin.getConfigLoader().getMainMenuConfig().getEnchantmentSlots().size()).doubleValue())).intValue()-1;
                    if (page + 1 <= maxPage) {
                        plugin.getMainGUI().openInventory((Player) event.getWhoClicked(), page + 1);
                    }
                } else if (plugin.getConfigLoader().getMainMenuConfig().getPreviousPageSlots().contains(event.getSlot())) {
                    int page = MainGUIWrapper.getPage(inv);
                    if (page > 0) {
                        plugin.getMainGUI().openInventory((Player) event.getWhoClicked(), page - 1);
                    }
                } else if (plugin.getConfigLoader().getMainMenuConfig().getEnchantmentSlots().contains(event.getSlot())) {
                    if (event.getCurrentItem() != null) {
                        NBTItem nbt = new NBTItem(event.getCurrentItem());
                        if (nbt.hasTag("eesID")) {
                            String id = nbt.getString("eesID");
                            EcoEnchant enchant = EcoEnchants.INSTANCE.getByID(id);
                            if (enchant != null) {
                                plugin.getLevelGUI().openInventory((Player) event.getWhoClicked(), enchant);
                            }
                        }
                    }
                }
            }
        }
    }

}
