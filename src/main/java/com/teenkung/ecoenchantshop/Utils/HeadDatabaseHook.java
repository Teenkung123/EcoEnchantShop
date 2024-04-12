package com.teenkung.ecoenchantshop.Utils;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadDatabaseHook implements Listener {

    private final EcoEnchantShop plugin;

    public HeadDatabaseHook(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
        plugin.reload();
    }



}
