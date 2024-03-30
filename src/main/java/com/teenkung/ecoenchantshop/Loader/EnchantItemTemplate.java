package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;

public class EnchantItemTemplate {

    private final String name;
    private final ArrayList<String> lore = new ArrayList<>();
    private final String rarityCommon;
    private final String rarityRare;
    private final String rarityEpic;
    private final String rarityLegendary;
    private final String raritySpecial;
    private final String rarityVerySpecial;
    private final String conflictNone;
    private final String conflictEverything;
    private final String conflictType;
    private final String priceSingle;
    private final String priceMultiple;

    public EnchantItemTemplate(EcoEnchantShop plugin, ConfigurationSection config) {
        this.name = config.getString("Name", "<yellow><name>");
        this.lore.addAll(config.getStringList("Lore"));
        this.rarityCommon = config.getString("Rarity.Common");
        this.rarityRare = config.getString("Rarity.Rare");
        this.rarityEpic = config.getString("Rarity.Epic");
        this.rarityLegendary = config.getString("Rarity.Legendary");
        this.raritySpecial = config.getString("Rarity.Special");
        this.rarityVerySpecial = config.getString("Rarity.VerySpecial");

        this.conflictNone = config.getString("Conflicts.NoConflicts");
        this.conflictEverything = config.getString("Conflicts.Everything");
        this.conflictType = config.getString("Conflicts.DisplayType");

        this.priceSingle = config.getString("Price.Single");
        this.priceMultiple = config.getString("Price.Multiple");
    }

    public String getName() { return name; }
    public ArrayList<String> getLore() { return lore; }
    public String getRarityCommon() { return rarityCommon; }
    public String getRarityRare() { return rarityRare; }
    public String getRarityEpic() { return rarityEpic; }
    public String getRarityLegendary() { return rarityLegendary; }
    public String getRaritySpecial() { return raritySpecial; }
    public String getRarityVerySpecial() { return rarityVerySpecial; }
    public String getConflictNone() { return conflictNone; }
    public String getConflictEverything() { return conflictEverything; }
    public String getConflictType() { return conflictType; }
    public String getPriceSingle() { return priceSingle; }
    public String getPriceMultiple() { return priceMultiple; }

}
