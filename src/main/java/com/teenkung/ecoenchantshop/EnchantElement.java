package com.teenkung.ecoenchantshop;

import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import net.advancedplugins.ae.api.AEAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EnchantElement {

    private final EnchantmentType type;
    private final String enchantment;
    private final EcoEnchant eco;
    private final Enchantment vanilla;

    public EnchantElement(@NotNull String id, @NotNull EnchantmentType type) {
        this.type = type;
        this.enchantment = id;
        if (type == EnchantmentType.ECOENCHANT) {
            eco = EcoEnchants.INSTANCE.getByID(enchantment);
            if (eco == null) {
                throw new IllegalArgumentException("EcoEnchant " + enchantment + " not found");
            }
            vanilla = eco.getEnchantment();
        } else {
            eco = null;
            vanilla = Enchantment.getByKey(NamespacedKey.minecraft(id));
            if (vanilla == null) {
                throw new IllegalArgumentException("Enchantment " + id + " not found");
            }
        }
    }

    public List<String> getDescription(int level, @Nullable Player player) {
        if (type.equals(EnchantmentType.ECOENCHANT)) {
            return Collections.singletonList(eco.getRawDescription(level, player));
        } else if (type == EnchantmentType.ADVANCED_ENCHANTMENT) {
            return AEAPI.getEnchantLore(enchantment, level);
        }
        return null;
    }

    public String getDisplayName() {
        if (type == EnchantmentType.ADVANCED_ENCHANTMENT) {
            return enchantment;
        } else if (type == EnchantmentType.ECOENCHANT) {
            return eco.getRawDisplayName();
        } else {
            return vanilla.getKey().examinableName();
        }
    }

    public Integer getMaxLevel() {
        if (type == EnchantmentType.ECOENCHANT) {
            return eco.getMaximumLevel();
        } else if (type == EnchantmentType.ADVANCED_ENCHANTMENT) {
            return AEAPI.getHighestEnchantmentLevel(enchantment);
        }
        return vanilla.getMaxLevel();
    }

}
