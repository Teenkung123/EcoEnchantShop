package com.teenkung.ecoenchantshop.GUI.Handlers;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.MainGUIWrapper;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.MainMenuConfig;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MainHandler implements Listener {

    private final EcoEnchantShop plugin;
    private boolean give = true;
    public MainHandler(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (MainGUIWrapper.isPluginGUI(event.getInventory())) {
            MainMenuConfig menuConfig = plugin.getConfigLoader().getMainMenuConfig();
            ItemStack ret = event.getInventory().getItem(menuConfig.getItemSearchSlot());
            if (ret != null && give) {
                event.getPlayer().getInventory().addItem(ret);
            }
            MainGUIWrapper.removeInventory(event.getInventory());
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (MainGUIWrapper.isPluginGUI(event.getClickedInventory())) {
                MainMenuConfig menuConfig = plugin.getConfigLoader().getMainMenuConfig();
                ItemStack stack = event.getClickedInventory().getItem(menuConfig.getItemSearchSlot());
                Inventory inv = event.getClickedInventory();
                Player player = (Player) event.getWhoClicked();
                event.setCancelled(true);
                if (menuConfig.getNextPageSlots().contains(event.getSlot())) {
                    ItemStack search = inv.getItem(plugin.getConfigLoader().getMainMenuConfig().getItemSearchSlot());
                    ArrayList<EcoEnchant> a = new ArrayList<>();
                    if (search != null) {
                        for (EcoEnchant e : plugin.getEnchantmentPrice().getAvailableEnchantments()) {
                            if (e.canEnchantItem(search)) {
                                a.add(e);
                            }
                        }
                    } else {
                        a.addAll(plugin.getEnchantmentPrice().getAvailableEnchantments());
                    }
                    int page = MainGUIWrapper.getPage(inv);
                    int maxPage = Double.valueOf(Math.ceil(Integer.valueOf(a.size()).doubleValue() / plugin.getConfigLoader().getMainMenuConfig().getEnchantmentSlots().size())).intValue() - 1;
                    if (page + 1 <= maxPage) {
                        plugin.getSoundLoader().playSound(player, "NextPage");
                        give = false;
                        plugin.getMainGUI().openInventory(player, page + 1, stack);
                        give = true;
                    }
                } else if (menuConfig.getPreviousPageSlots().contains(event.getSlot())) {
                    int page = MainGUIWrapper.getPage(inv);
                    if (page > 0) {
                        plugin.getSoundLoader().playSound(player, "PreviousPage");
                        give = false;
                        plugin.getMainGUI().openInventory(player, page - 1, stack);
                        give = true;
                    }
                } else if (menuConfig.getEnchantmentSlots().contains(event.getSlot())) {
                    if (event.getCurrentItem() != null) {
                        NBTItem nbt = new NBTItem(event.getCurrentItem());
                        if (nbt.hasTag("eesID")) {
                            String id = nbt.getString("eesID");
                            EcoEnchant enchant = EcoEnchants.INSTANCE.getByID(id);
                            if (enchant != null) {
                                plugin.getSoundLoader().playSound(player, "Click");
                                give = false;
                                plugin.getLevelGUI().openInventory(player, enchant, event.getClickedInventory().getItem(menuConfig.getItemSearchSlot()));
                                give = true;
                            }
                        }
                    }
                } else if (menuConfig.getItemSearchSlot() == event.getSlot()) {
                    if (event.getCurrentItem() != null) {
                        plugin.getSoundLoader().playSound(player, "SearchItemOut");
                        plugin.getMainGUI().openInventory(player, 0, null);
                    }
                }
            } else if (MainGUIWrapper.isPluginGUI(event.getWhoClicked().getOpenInventory().getTopInventory())) {
                event.setCancelled(true);
                Inventory inv = event.getWhoClicked().getOpenInventory().getTopInventory();
                Player player = (Player) event.getWhoClicked();
                int page = MainGUIWrapper.getPage(inv);
                if (event.getCurrentItem() != null) {
                    plugin.getSoundLoader().playSound(player, "SearchItem");
                    give = false;
                    plugin.getMainGUI().openInventory(player, page, event.getCurrentItem());
                    give = true;
                    event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                }
            }
        }
    }

}
