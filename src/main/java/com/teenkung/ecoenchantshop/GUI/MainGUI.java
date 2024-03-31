package com.teenkung.ecoenchantshop.GUI;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.MainGUIWrapper;
import com.teenkung.ecoenchantshop.Loader.EnchantItemTemplate;
import com.teenkung.ecoenchantshop.Loader.MenuConfig.MainMenuConfig;
import com.teenkung.ecoenchantshop.Utils.Utils;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.target.EnchantmentTarget;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
    private final MainMenuConfig config;

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
        int ench = page*config.getEnchantmentSlots().size();
        int index = 0;
        for (String str : config.getLayout()) {
            for (int i = 0; i < 9; i++) {
                if (config.getEnchantmentSlots().contains(index) && (ench + 1 < plugin.getEnchantmentPrice().getAvailableEnchantments().size())) {
                    inv.setItem(index, createItem(player, new ArrayList<>(plugin.getEnchantmentPrice().getAvailableEnchantments()).get(ench)));
                    ench++;
                } else {
                    String c = String.valueOf(str.charAt(i));
                    ItemStack stack = config.getItemMap().get(c);
                    if (stack != null) {
                        inv.setItem(index, stack);
                    }
                }
                index++;
            }
        }
    }

    private ItemStack createItem(Player player, EcoEnchant enchant) {
        EnchantItemTemplate templateConfig = plugin.getConfigLoader().getEnchantTemplate();

        String rarity = enchant.getEnchantmentRarity().getDisplayName().toLowerCase();
        rarity = switch (rarity) {
            case "very special" -> templateConfig.getRarityVerySpecial();
            case "special" -> templateConfig.getRaritySpecial();
            case "legendary" -> templateConfig.getRarityLegendary();
            case "epic" -> templateConfig.getRarityEpic();
            case "rare" -> templateConfig.getRarityRare();
            case "uncommon" -> templateConfig.getRarityUncommon();
            case "common" -> templateConfig.getRarityCommon();
            default -> "<red>Unknown Rarity";
        };

        Component applicable = Component.empty();
        for (EnchantmentTarget target : enchant.getTargets()) {
            applicable = applicable.append(MiniMessage.miniMessage().deserialize(target.getDisplayName() + " "));
        }

        Component conflict = Component.empty();
        if (enchant.getConflictsWithEverything()) {
            conflict = MiniMessage.miniMessage().deserialize(templateConfig.getConflictEverything());
        } else if (enchant.getConflicts().isEmpty()) {
            conflict = MiniMessage.miniMessage().deserialize(templateConfig.getConflictNone());
        } else {
            for (Enchantment conflictEnchant : enchant.getConflicts()) {
                String componentAsString = PlainTextComponentSerializer.plainText().serialize(conflictEnchant.displayName(1));
                componentAsString = componentAsString.replaceAll(" I", "");
                //BUG: Component.newLine() does not work as intended.
                //if (templateConfig.getConflictType().equalsIgnoreCase("VERTICAL")) {
                //    conflict = conflict.append(MiniMessage.miniMessage().deserialize("<white>  - <gray>" + componentAsString));
                //} else {
                // Check if 'conflict' already has content; if so, prepend a comma and space.
                if (!PlainTextComponentSerializer.plainText().serialize(conflict).isEmpty()) {
                    componentAsString = ", " + componentAsString;
                }
                conflict = conflict.append(MiniMessage.miniMessage().deserialize("<gray>" + componentAsString));
                //}
            }
        }

        Component price;
        if (enchant.getMaximumLevel() == 1) {
            Component p = MiniMessage.miniMessage().deserialize(String.valueOf(plugin.getEnchantmentPrice().getHolders(enchant).getPrice(1)));
            price = MiniMessage.miniMessage().deserialize(templateConfig.getPriceSingle(),
                    Placeholder.component("price", p));
        } else {
            Component lp = MiniMessage.miniMessage().deserialize(String.valueOf(plugin.getEnchantmentPrice().getHolders(enchant).getPrice(1)));
            Component mp = MiniMessage.miniMessage().deserialize(String.valueOf(plugin.getEnchantmentPrice().getHolders(enchant).getPrice(enchant.getMaximumLevel())));
            price = MiniMessage.miniMessage().deserialize(templateConfig.getPriceMultiple(),
                    Placeholder.component("min_price", lp),
                    Placeholder.component("max_price", mp));
        }

        ArrayList<Component> lores = new ArrayList<>();
        for (String lore : templateConfig.getLore()) {
            lores.add(MiniMessage.miniMessage().deserialize(lore,
                    Placeholder.component("description", MiniMessage.miniMessage().deserialize("<dark_grey>"+ Utils.transformLegacyEnchantmentDescription(enchant.getRawDescription(1, player)))),
                    Placeholder.component("rarity", MiniMessage.miniMessage().deserialize(rarity)),
                    Placeholder.component("max_level", MiniMessage.miniMessage().deserialize(String.valueOf(enchant.getMaximumLevel()))),
                    Placeholder.component("applicable", applicable),
                    Placeholder.component("conflicts", conflict),
                    Placeholder.component("price", price)
            ));
        }


        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            Component display = MiniMessage.miniMessage().deserialize("<i:false>" + templateConfig.getName(), Placeholder.unparsed("name", enchant.getRawDisplayName()));
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
