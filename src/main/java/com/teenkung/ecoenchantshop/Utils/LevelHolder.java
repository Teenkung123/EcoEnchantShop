package com.teenkung.ecoenchantshop.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LevelHolder {

    private final Map<Integer, Double> levels = new HashMap<>();

    public LevelHolder() {}

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

}
