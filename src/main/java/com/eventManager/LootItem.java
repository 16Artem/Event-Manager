package com.eventManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a loot item with material type, quantity range, and rarity tier.
 */
public class LootItem {
    private final Material material;
    private final int minAmount;
    private final int maxAmount;
    private final LootRarity rarity;

    public LootItem(Material material, int minAmount, int maxAmount, LootRarity rarity) {
        this.material = material;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rarity = rarity;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public LootRarity getRarity() {
        return rarity;
    }

    public ItemStack createItemStack(int amount) {
        return new ItemStack(material, amount);
    }
}
