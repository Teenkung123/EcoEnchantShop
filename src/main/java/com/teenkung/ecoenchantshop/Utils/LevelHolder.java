package com.teenkung.ecoenchantshop.Utils;

import com.willfp.ecoenchants.enchant.EcoEnchant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class LevelHolder {

    private final Map<Integer, Double> levels = new HashMap<>();
    private final EcoEnchant enchant;

    public LevelHolder(EcoEnchant enchant) {
        this.enchant = enchant;
    }

    public void addLevel(Integer level, Double price) {
        levels.put(level, price);
    }

    public void removeLevel(Integer level) {
        levels.remove(level);
    }

    public Set<Integer> getAllLevels() {
        return levels.keySet();
    }

    public Double getPrice(Integer level) {
        return levels.get(level);
    }
    public EcoEnchant getEnchant() { return enchant; }

}
