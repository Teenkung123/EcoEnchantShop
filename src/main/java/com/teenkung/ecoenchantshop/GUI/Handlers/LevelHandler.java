package com.teenkung.ecoenchantshop.GUI.Handlers;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.LevelGUIWrapper;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.LevelMenuConfig;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.MainMenuConfig;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LevelHandler implements Listener {

    private final EcoEnchantShop plugin;
    private boolean give = true;
    public LevelHandler(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (LevelGUIWrapper.isPluginGUI(event.getInventory())) {
            ItemStack ret = LevelGUIWrapper.getSearch(event.getInventory());
            if (ret != null && give) {
                event.getPlayer().getInventory().addItem(ret);
            }
            LevelGUIWrapper.removeInventory(event.getInventory());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (LevelGUIWrapper.isPluginGUI(event.getClickedInventory())) {
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
                            give = false;
                            plugin.getConfirmGUI().openInventory((Player) event.getWhoClicked(), enchant, level, LevelGUIWrapper.getSearch(event.getClickedInventory()));
                            give = true;
                        }
                    }
                }
            }
        }
    }

}
