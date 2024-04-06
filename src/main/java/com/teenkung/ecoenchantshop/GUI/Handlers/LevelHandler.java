package com.teenkung.ecoenchantshop.GUI.Handlers;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.LevelGUIWrapper;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class LevelHandler implements Listener {

    private final EcoEnchantShop plugin;

    public LevelHandler(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (LevelGUIWrapper.isPluginGUI(event.getInventory())) {
            LevelGUIWrapper.removeInventory(event.getInventory());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (LevelGUIWrapper.isPluginGUI(event.getInventory())) {
            event.setCancelled(true);
            if (plugin.getConfigLoader().getLevelMenuConfig().getSlots().contains(event.getSlot())) {
                if (event.getCurrentItem() != null) {
                    NBTItem nbt = new NBTItem(event.getCurrentItem());
                    if (nbt.hasTag("eesID")) {
                        String id = nbt.getString("eesID");
                        Integer level = nbt.getInteger("eesLevel");
                        EcoEnchant enchant = EcoEnchants.INSTANCE.getByID(id);
                        if (enchant != null) {
                            plugin.getSoundLoader().playSound((Player) event.getWhoClicked(), "Click");
                            plugin.getConfirmGUI().openInventory((Player) event.getWhoClicked(), enchant, level);
                        }
                    }
                }
            }
        }
    }

}
