package com.teenkung.ecoenchantshop.GUI.Handlers;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.ConfirmGUIWrapper;
import com.teenkung.ecoenchantshop.GUI.Wrapper.LevelGUIWrapper;
import com.teenkung.ecoenchantshop.Utils.LevelHolder;
import com.willfp.eco.core.items.builder.EnchantedBookBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmHandler implements Listener {

    private final EcoEnchantShop plugin;
    private boolean give = true;
    public ConfirmHandler(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (ConfirmGUIWrapper.isPluginGUI(event.getInventory())) {
            ItemStack ret = ConfirmGUIWrapper.getSearch(event.getInventory());
            if (ret != null && give) {
                event.getPlayer().getInventory().addItem(ret);
            }
            ConfirmGUIWrapper.removeInventory(event.getInventory());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (ConfirmGUIWrapper.isPluginGUI(event.getClickedInventory())) {
            event.setCancelled(true);
            LevelHolder holder = ConfirmGUIWrapper.getLevelHolder(event.getClickedInventory());
            Player player = (Player) event.getWhoClicked();
            if (plugin.getConfigLoader().getConfirmMenuConfig().getAcceptSlots().contains(event.getSlot())) {
                Integer level = ConfirmGUIWrapper.getLevel(event.getClickedInventory());
                Double price = holder.getPrice(level);
                Economy economy = plugin.getEconomy();
                if (economy.has(player, price)) {
                    if (player.getInventory().firstEmpty() != -1) {
                        //ItemStack stack = new EnchantedBookBuilder().addStoredEnchantment(holder.getEnchant().getEnchantment(), level).build();
                        economy.withdrawPlayer(player, price);
                        ItemStack item = ConfirmGUIWrapper.getSearch(event.getClickedInventory());
                        item.addEnchantment(holder.getEnchant().getEnchantment(), level);
                        //player.getInventory().addItem(stack);
                        plugin.getSoundLoader().playSound(player, "BuyItem");
                        player.sendMessage(
                                MiniMessage.miniMessage().deserialize(
                                        plugin.getMessageLoader().getBoughtItem(),
                                        Placeholder.unparsed("enchant", holder.getEnchant().getRawDisplayName()),
                                        Placeholder.unparsed("level", String.valueOf(level)),
                                        Placeholder.unparsed("price", String.valueOf(price)),
                                        Placeholder.unparsed("amount", String.valueOf(1))
                                )
                        );
                        ConfirmGUIWrapper.removeInventory(event.getClickedInventory());
                        give = false;
                        plugin.getMainGUI().openInventory(player, 0, item);
                        give = true;
                    } else {
                        plugin.getSoundLoader().playSound(player, "FailedToBuy");
                        player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.getMessageLoader().getNotEnoughInventory()));
                    }
                } else {
                    plugin.getSoundLoader().playSound(player, "FailedToBuy");
                    player.sendMessage(
                            MiniMessage.miniMessage().deserialize(
                                    plugin.getMessageLoader().getNotEnoughMoney(),
                                    Placeholder.unparsed("balance", String.valueOf(economy.getBalance(player))),
                                    Placeholder.unparsed("price", String.valueOf(price))
                            )
                    );
                }
            } else if (plugin.getConfigLoader().getConfirmMenuConfig().getDenySlots().contains(event.getSlot())) {
                plugin.getSoundLoader().playSound(player, "Click");
                give = false;
                plugin.getLevelGUI().openInventory(player ,holder.getEnchant(), ConfirmGUIWrapper.getSearch(event.getClickedInventory()));
                give = true;
            }
        }
    }

}
