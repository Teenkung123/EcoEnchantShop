package com.teenkung.ecoenchantshop.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TabCommand implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ArrayList<String> result = new ArrayList<>();
        if (strings.length == 1) {
            if (commandSender.hasPermission("ecoenchantshop.use") && commandSender instanceof Player) {
                result.add("menu");
            }
            if (commandSender.hasPermission("ecoenchantshop.reload")) {
                result.add("reload");
            }
        }

        return result;
    }
}
