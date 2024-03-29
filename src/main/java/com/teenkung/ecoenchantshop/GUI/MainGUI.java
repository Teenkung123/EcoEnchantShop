package com.teenkung.ecoenchantshop.GUI;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.MainGUIWrapper;
import com.teenkung.ecoenchantshop.Loader.MainMenuConfig;
import com.teenkung.ecoenchantshop.Utils.Utils;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.target.EnchantmentTarget;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MainGUI {

    private final EcoEnchantShop plugin;
    private MainMenuConfig config;

    public MainGUI(EcoEnchantShop plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigLoader().getMainMenuConfig();
    }

    public void openInventory(Player player, Integer page) {
        Inventory inv = Bukkit.createInventory(null, 9*config.getLayout().size(), MiniMessage.miniMessage().deserialize(config.getTitle()));
        MainGUIWrapper.addInventory(inv, page);
        createSlots(inv, player, page);
        player.openInventory(inv);
    }

    private void createSlots(Inventory inv, Player player, Integer page) {
        int index = page*config.getEnchantmentSlots().size();
        for (int i = 0 ; i < inv.getSize() ; i++) {
            if (config.getEnchantmentSlots().contains(i)) {
                if (index + 1 < plugin.getEnchantmentPrice().getAvailableEnchantments().size()) {
                    inv.setItem(i, createItem(player, new ArrayList<>(plugin.getEnchantmentPrice().getAvailableEnchantments()).get(index)));
                    index++;
                }
            } else {
                inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
    }

    private ItemStack createItem(Player player, EcoEnchant enchant) {
        ArrayList<Component> lores = new ArrayList<>();
        lores.add(MiniMessage.miniMessage().deserialize("<dark_grey>"+ Utils.transformLegacyEnchantmentDescription(enchant.getRawDescription(1, player))));
        lores.add(Component.space());

        String rarity = enchant.getEnchantmentRarity().getDisplayName().toLowerCase();
        String rarity_prefix = "<white>Rarity: ";
        switch (rarity) {
            case "very special":
                lores.add(MiniMessage.miniMessage().deserialize(rarity_prefix + "<light_purple>Very Special"));
                break;
            case "special":
                lores.add(MiniMessage.miniMessage().deserialize(rarity_prefix + "<blue>Special"));
                break;
            case "legendary":
                lores.add(MiniMessage.miniMessage().deserialize(rarity_prefix + "<gold>Legendary"));
                break;
            case "epic":
                lores.add(MiniMessage.miniMessage().deserialize(rarity_prefix + "<light_purple>Epic"));
                break;
            case "rare":
                lores.add(MiniMessage.miniMessage().deserialize(rarity_prefix + "<aqua>Rare"));
                break;
            case "common":
                lores.add(MiniMessage.miniMessage().deserialize(rarity_prefix + "<gray>Common"));
                break;
            default:
                lores.add(MiniMessage.miniMessage().deserialize(rarity_prefix + "<red>Unknown"));
                break;
        }

        lores.add(MiniMessage.miniMessage().deserialize("<white>Max Level: <green>" + enchant.getMaximumLevel()));
        Component applicable = MiniMessage.miniMessage().deserialize("<white>Applicable to: <green>");
        for (EnchantmentTarget target : enchant.getTargets()) {
            applicable = applicable.append(MiniMessage.miniMessage().deserialize(target.getDisplayName() + " "));
        }
        lores.add(applicable);


        if (enchant.getConflictsWithEverything()) {
            // If the enchantment conflicts with everything, add this information directly
            lores.add(MiniMessage.miniMessage().deserialize("<white>Conflicts with: <red>Every Enchantments"));
        } else if (!enchant.getConflicts().isEmpty()) {
            // Start with the "Conflicts with:" line
            Component conflictHeader = MiniMessage.miniMessage().deserialize("<white>Conflicts with:");
            lores.add(conflictHeader);

            // For each conflict, add it in a new line with a dash
            for (Enchantment conflictEnchant : enchant.getConflicts()) {
                String componentAsString = PlainTextComponentSerializer.plainText().serialize(conflictEnchant.displayName(1));
                componentAsString = componentAsString.replaceAll(" I", "");

                Component conflictLine = MiniMessage.miniMessage().deserialize("<white>  - <gray>" + componentAsString);
                lores.add(conflictLine);
            }
        } else {
            lores.add(MiniMessage.miniMessage().deserialize("<white>Conflicts with: <green>No Conflict"));
        }
        lores.add(Component.space());
        if (enchant.getMaximumLevel() == 1) {
            lores.add(MiniMessage.miniMessage().deserialize("<white>Price: <yellow>$" + plugin.getEnchantmentPrice().getHolders(enchant).getPrice(1)));
        } else {
            lores.add(MiniMessage.miniMessage().deserialize("<white>Price: <yellow>$" + plugin.getEnchantmentPrice().getHolders(enchant).getPrice(1) + " - $" + plugin.getEnchantmentPrice().getHolders(enchant).getPrice(enchant.getMaximumLevel())));
        }

        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            Component display = MiniMessage.miniMessage().deserialize("<i:false>" + enchant.getRawDisplayName());
            meta.displayName(display);
            meta.lore(lores); // Set the lore on the item meta
            stack.setItemMeta(meta);
        }
        NBTItem nbt = new NBTItem(stack);
        nbt.setBoolean("eesClickable", true);
        nbt.setString("eesID", enchant.getID());
        nbt.applyNBT(stack);
        return stack;
    }


}
