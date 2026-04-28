package com.eventManager;

import org.bukkit.ChatColor;

/**
 * Enum representing loot rarity tiers with associated display properties and drop weights.
 * Weight determines spawn probability using weighted random selection.
 */
public enum LootRarity {
    COMMON(ChatColor.WHITE, "Обычный", 50.0),
    RARE(ChatColor.BLUE, "Редкий", 30.0),
    EPIC(ChatColor.DARK_PURPLE, "Эпический", 15.0),
    LEGENDARY(ChatColor.GOLD, "Легендарный", 5.0);

    private final ChatColor color;
    private final String displayName;
    private final double weight;

    LootRarity(ChatColor color, String displayName, double weight) {
        this.color = color;
        this.displayName = displayName;
        this.weight = weight;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getWeight() {
        return weight;
    }

    public String getColoredName() {
        return color + displayName;
    }
}
