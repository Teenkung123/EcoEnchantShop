package com.teenkung.ecoenchantshop.Commands;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final EcoEnchantShop plugin;

    public MainCommand(EcoEnchantShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            plugin.getSoundLoader().playSound(player, "OpenMenu");
            plugin.getMainGUI().openInventory(player, 0, null);
        }

        return false;
    }
}
