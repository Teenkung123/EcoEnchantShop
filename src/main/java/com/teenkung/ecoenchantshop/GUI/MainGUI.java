package com.teenkung.ecoenchantshop.GUI;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.GUI.Wrapper.MainGUIWrapper;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import com.willfp.ecoenchants.rarity.EnchantmentRarities;
import com.willfp.ecoenchants.rarity.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainGUI {

    private final EcoEnchantShop plugin;
    private final Map<Integer, Inventory> inventory = new HashMap<>();
    private final ArrayList<Integer> freeSlot = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34));

    public MainGUI(EcoEnchantShop plugin) {
        this.plugin = plugin;
        loadGUI();
    }

    public void loadGUI() {
        int size = plugin.getEnchantmentPrice().getAvailableEnchantments().size();
        int pages = Double.valueOf(Math.ceil((double) size /21)).intValue();
        for (int i = 0; i < pages; i++) {
            Inventory inv = Bukkit.createInventory(null, 45);
            buildLayout(inv);
            fillItems(inv, i);
            MainGUIWrapper.addInventory(inv);
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
        int startIndex = page * 21; // Starting index for enchantments in this page
        ArrayList<EcoEnchant> enchants = new ArrayList<>(plugin.getEnchantmentPrice().getAvailableEnchantments());
        for (int i = 0; i < 21; i++) {
            if (startIndex + i >= enchants.size()) {
                break; // Break if there are no more enchantments to display
            }
            EcoEnchant enchant = enchants.get(startIndex + i);
            ItemStack itemStack = createItemStack(enchant);
            if (freeSlot.size() > i) { // Check to avoid IndexOutOfBoundsException for freeSlot
                int inventoryIndex = freeSlot.get(i); // Get the actual inventory slot to place the item
                inv.setItem(inventoryIndex, itemStack); // Place the item in the correct slot
                MainGUIWrapper.addInventoryItem(inv, inventoryIndex, enchant); // Assuming you want to track this for some reason
            }
        }
    }


    private ItemStack createItemStack(EcoEnchant enchant) {

        ArrayList<Component> lores = new ArrayList<>();

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

        if (enchant.getConflictsWithEverything()) {
            // If the enchantment conflicts with everything, add this information directly
            lores.add(MiniMessage.miniMessage().deserialize("<white>Conflicts with: \n  - Everything"));
        } else if (!enchant.getConflicts().isEmpty()) {
            // Start with the "Conflicts with:" line
            Component conflictHeader = MiniMessage.miniMessage().deserialize("<gray>Conflicts with:");
            lores.add(conflictHeader);

            // For each conflict, add it in a new line with a dash
            for (Enchantment conflictEnchant : enchant.getConflicts()) {
                // Assuming displayName(1) returns a string, adjust if it returns a Component
                Component conflictLine = MiniMessage.miniMessage().deserialize("<gray>  - ").append(conflictEnchant.displayName(1));
                lores.add(conflictLine);
            }
        }

        ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            Component display = MiniMessage.miniMessage().deserialize("<i:false>" + enchant.getRawDisplayName());
            meta.displayName(display);
            meta.lore(lores); // Set the lore on the item meta
            stack.setItemMeta(meta);
        }
        return stack;
    }


    public void openInventory(Player player) {
        if (inventory.containsKey(0)) {
            player.openInventory(inventory.get(0));
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Something went wrong! the plugin cannot detect any Main GUI Page!"));
        }
    }

}
