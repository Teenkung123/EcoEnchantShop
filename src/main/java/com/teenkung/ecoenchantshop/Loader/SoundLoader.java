package com.teenkung.ecoenchantshop.Loader;

import com.teenkung.ecoenchantshop.EcoEnchantShop;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class SoundLoader {

    private final EcoEnchantShop plugin;
    private final FileConfiguration config;

    public SoundLoader(EcoEnchantShop plugin) {
        this.plugin = plugin;
        // Ensures the sounds.yml file is created from the resources if it doesn't exist.
        plugin.saveResource("sounds.yml", false);
        // Load the sounds.yml file.
        File soundFile = new File(plugin.getDataFolder(), "sounds.yml");
        this.config = YamlConfiguration.loadConfiguration(soundFile);
    }

    public void playSound(Player player, String soundIdentifier) {
        // Retrieves a list of sound configurations for the given identifier.
        List<String> soundConfigs = config.getStringList(soundIdentifier);
        if (!soundConfigs.isEmpty()) {
            for (String soundConfig : soundConfigs) {
                try {
                    // Splits the sound configuration into its components.
                    String[] parts = soundConfig.split(":");
                    if (parts.length == 3) {
                        // Parses the sound name, volume, and pitch from the configuration.
                        String soundName = parts[0];
                        float volume = Float.parseFloat(parts[1]);
                        float pitch = Float.parseFloat(parts[2]);
                        Sound sound = Sound.valueOf(soundName.toUpperCase());
                        // Plays the sound at the player's location.
                        player.playSound(player.getLocation(), sound, volume, pitch);
                    } else {
                        // Logs a warning if the sound configuration format is incorrect.
                        plugin.getLogger().warning("Invalid sound configuration format for: " + soundIdentifier + " with details: " + soundConfig);
                    }
                } catch (IllegalArgumentException e) {
                    // Catches any errors from invalid sound names or parsing errors.
                    plugin.getLogger().warning("Invalid sound configuration for: " + soundIdentifier + " with details: " + soundConfig);
                }
            }
        } else {
            // Logs information if no sounds are configured for the identifier.
            plugin.getLogger().info("No sounds configured for: " + soundIdentifier);
        }
    }
}
