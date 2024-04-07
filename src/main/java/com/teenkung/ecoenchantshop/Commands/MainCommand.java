package com.teenkung.ecoenchantshop.Commands;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
            if (strings.length != 0) {
                if (strings[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("ecoenchantshop.reload")) {
                        long ms = System.currentTimeMillis();
                        player.sendMessage(MiniMessage.miniMessage().deserialize(
                                plugin.getMessageLoader().getReloadMessage()
                        ));
                        plugin.reload();
                        player.sendMessage(MiniMessage.miniMessage().deserialize(
                                plugin.getMessageLoader().getReloadedMessage(),
                                Placeholder.unparsed("ms", String.valueOf(System.currentTimeMillis() - ms))
                        ));
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize(
                                plugin.getMessageLoader().getNoPermission()
                        ));
                    }
                } else {
                    openInventory(player);
                }
            } else {
                openInventory(player);
            }

        }

        return false;
    }

    private void openInventory(Player player) {
        if (player.hasPermission("ecoenchantshop.use")) {
            plugin.getSoundLoader().playSound(player, "OpenMenu");
            plugin.getMainGUI().openInventory(player, 0, null);
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(
                    plugin.getMessageLoader().getNoPermission()
            ));
        }
    }
}
