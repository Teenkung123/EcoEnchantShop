package com.teenkung.ecoenchantshop.Loader.MenuConfig;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public interface MenuConfig {
    String getTitle();
    ArrayList<String> getLayout();
    Map<String, ItemStack> getItemMap();

}
