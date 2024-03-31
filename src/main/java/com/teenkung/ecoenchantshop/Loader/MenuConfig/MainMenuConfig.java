package com.teenkung.ecoenchantshop.Loader.MenuConfig;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.Utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainMenuConfig implements MenuConfig {

    private final String title;
    private final ArrayList<String> layout = new ArrayList<>();
    private final Map<String, ItemStack> itemMap = new HashMap<>();
    private final ArrayList<Integer> enchantmentSlot = new ArrayList<>();
    private final ArrayList<Integer> nextPageSlot = new ArrayList<>();
    private final ArrayList<Integer> previousPageSlot = new ArrayList<>();

    public MainMenuConfig(EcoEnchantShop plugin, ConfigurationSection config) {
        plugin.getLogger().info("Loading Main Menu GUIs. . .");
        this.title = config.getString("Title", "Enchantment Shop Menu");
        this.layout.addAll(config.getStringList("Layout.Index"));
        ConfigurationSection section = config.getConfigurationSection("Layout.Items");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection itemConfig = section.getConfigurationSection(key);
                ItemStack stack = Utils.createItem(itemConfig);
                itemMap.put(key, stack);
            }
        }
        this.enchantmentSlot.addAll(config.getIntegerList("Layout.EnchantmentSlot"));
        this.nextPageSlot.addAll(config.getIntegerList("Layout.NextPageSlot"));
        this.previousPageSlot.addAll(config.getIntegerList("Layout.PreviousPageSlot"));
    }

    @Override
    public String getTitle() { return title; }
    @Override
    public ArrayList<String> getLayout() { return layout; }
    @Override
    public Map<String, ItemStack> getItemMap() { return itemMap; }
    public ArrayList<Integer> getEnchantmentSlots() { return enchantmentSlot; }
    public ArrayList<Integer> getNextPageSlots() { return nextPageSlot; }
    public ArrayList<Integer> getPreviousPageSlots() { return previousPageSlot; }

}
