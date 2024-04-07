package com.teenkung.ecoenchantshop.GUI.Handlers;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.ConfirmGUIWrapper;
import com.teenkung.ecoenchantshop.Utils.LevelHolder;
import com.willfp.eco.core.items.builder.EnchantedBookBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class ConfirmHandler implements Listener {

    private final EcoEnchantShop plugin;

    public ConfirmHandler(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (ConfirmGUIWrapper.isPluginGUI(event.getInventory())) {
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
                    ItemStack stack = new EnchantedBookBuilder().addStoredEnchantment(holder.getEnchant().getEnchantment(), level).build();
                    if (player.getInventory().firstEmpty() != -1) {
                        economy.withdrawPlayer(player, price);
                        player.getInventory().addItem(stack);
                        player.closeInventory();
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
                plugin.getLevelGUI().openInventory(player ,holder.getEnchant());
            }
            event.setCancelled(true);
        }
    }

}
