package com.teenkung.ecoenchantshop.Loader.MenuConfig;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.Utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LevelMenuConfig implements MenuConfig {

    private final String title;
    private final ArrayList<String> layout = new ArrayList<>();
    private final Map<String, ItemStack> itemMap = new HashMap<>();
    private final ArrayList<Integer> slots = new ArrayList<>();

    public LevelMenuConfig(EcoEnchantShop plugin, ConfigurationSection config) {
        plugin.getLogger().info("Loading Main Level GUIs. . .");
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
        this.slots.addAll(config.getIntegerList("Layout.LevelSlots"));
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ArrayList<String> getLayout() {
        return layout;
    }

    @Override
    public Map<String, ItemStack> getItemMap() {
        return itemMap;
    }

    public ArrayList<Integer> getSlots() {
        return slots;
    }
}
