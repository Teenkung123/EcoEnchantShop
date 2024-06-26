package com.teenkung.ecoenchantshop.Loader.MenuConfig;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import com.teenkung.ecoenchantshop.Utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused"})
public class ConfirmMenuConfig{

    private final String title;
    private final ArrayList<String> layout = new ArrayList<>();
    private final Map<String, ItemStack> itemMap = new HashMap<>();
    private final Integer previewSlot;
    private final ArrayList<Integer> acceptSlots = new ArrayList<>();
    private final ArrayList<Integer> denySlots = new ArrayList<>();

    public ConfirmMenuConfig(EcoEnchantShop plugin, ConfigurationSection config) {
        plugin.getLogger().info("Loading Confirm GUIs. . .");
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
        this.previewSlot = config.getInt("Layout.PreviewSlot");
        this.acceptSlots.addAll(config.getIntegerList("Layout.AcceptSlots"));
        this.denySlots.addAll(config.getIntegerList("Layout.DenySlots"));
    }

    public String getTitle() {
        return title;
    }
    public ArrayList<String> getLayout() {
        return layout;
    }
    public Map<String, ItemStack> getItemMap() {
        return itemMap;
    }
    public Integer getPreviewSlot() { return previewSlot; }
    public ArrayList<Integer> getAcceptSlots() { return acceptSlots; }
    public ArrayList<Integer> getDenySlots() { return denySlots; }
}
