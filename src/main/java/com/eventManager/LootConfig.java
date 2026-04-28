package com.eventManager;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class LootConfig {
    
    // ============================================
    // НАСТРОЙКИ ВРЕМЕНИ СОБЫТИЙ
    // ============================================
    
    // Минимальное время между событиями (в секундах)
    public static final int MIN_EVENT_INTERVAL = 5400; // 1.5 часа (90 минут)
    
    // Максимальное время между событиями (в секундах)
    public static final int MAX_EVENT_INTERVAL = 5400; // 1.5 часа (90 минут)
    
    // Время до открытия сундука (в секундах)
    public static final int CHEST_OPEN_DELAY = 60; // 1 минута
    
    // Время жизни сундука после открытия (в секундах)
    public static final int CHEST_LIFETIME = 120; // 2 минуты
    
    
    // ============================================
    // НАСТРОЙКА ЛУТА ПО РЕДКОСТИ
    // ============================================
    
    public static List<LootItem> getCommonLoot() {
        List<LootItem> loot = new ArrayList<>();
        
        loot.add(new LootItem(Material.IRON_INGOT, 1, 5, LootRarity.COMMON));
        loot.add(new LootItem(Material.COAL, 5, 15, LootRarity.COMMON));
        loot.add(new LootItem(Material.BREAD, 3, 10, LootRarity.COMMON));
        loot.add(new LootItem(Material.ARROW, 10, 32, LootRarity.COMMON));
        loot.add(new LootItem(Material.COBBLESTONE, 10, 32, LootRarity.COMMON));
        loot.add(new LootItem(Material.OAK_LOG, 5, 16, LootRarity.COMMON));
        loot.add(new LootItem(Material.LEATHER, 2, 8, LootRarity.COMMON));
        
        return loot;
    }
    
    public static List<LootItem> getRareLoot() {
        List<LootItem> loot = new ArrayList<>();
        
        loot.add(new LootItem(Material.GOLD_INGOT, 1, 5, LootRarity.RARE));
        loot.add(new LootItem(Material.REDSTONE, 5, 15, LootRarity.RARE));
        loot.add(new LootItem(Material.LAPIS_LAZULI, 3, 10, LootRarity.RARE));
        loot.add(new LootItem(Material.ENDER_PEARL, 1, 3, LootRarity.RARE));
        loot.add(new LootItem(Material.IRON_SWORD, 1, 1, LootRarity.RARE));
        loot.add(new LootItem(Material.IRON_CHESTPLATE, 1, 1, LootRarity.RARE));
        loot.add(new LootItem(Material.GOLDEN_APPLE, 1, 3, LootRarity.RARE));
        loot.add(new LootItem(Material.EXPERIENCE_BOTTLE, 5, 15, LootRarity.RARE));
        
        return loot;
    }
    
    public static List<LootItem> getEpicLoot() {
        List<LootItem> loot = new ArrayList<>();
        
        loot.add(new LootItem(Material.DIAMOND, 1, 5, LootRarity.EPIC));
        loot.add(new LootItem(Material.EMERALD, 1, 3, LootRarity.EPIC));
        loot.add(new LootItem(Material.DIAMOND_SWORD, 1, 1, LootRarity.EPIC));
        loot.add(new LootItem(Material.DIAMOND_PICKAXE, 1, 1, LootRarity.EPIC));
        loot.add(new LootItem(Material.DIAMOND_CHESTPLATE, 1, 1, LootRarity.EPIC));
        loot.add(new LootItem(Material.ENCHANTED_GOLDEN_APPLE, 1, 2, LootRarity.EPIC));
        loot.add(new LootItem(Material.TOTEM_OF_UNDYING, 1, 1, LootRarity.EPIC));
        loot.add(new LootItem(Material.ELYTRA, 1, 1, LootRarity.EPIC));
        
        return loot;
    }
    
    public static List<LootItem> getLegendaryLoot() {
        List<LootItem> loot = new ArrayList<>();
        
        loot.add(new LootItem(Material.NETHERITE_INGOT, 1, 3, LootRarity.LEGENDARY));
        loot.add(new LootItem(Material.NETHERITE_SWORD, 1, 1, LootRarity.LEGENDARY));
        loot.add(new LootItem(Material.NETHERITE_CHESTPLATE, 1, 1, LootRarity.LEGENDARY));
        loot.add(new LootItem(Material.NETHERITE_PICKAXE, 1, 1, LootRarity.LEGENDARY));
        loot.add(new LootItem(Material.ENCHANTED_GOLDEN_APPLE, 2, 5, LootRarity.LEGENDARY));
        loot.add(new LootItem(Material.NETHER_STAR, 1, 2, LootRarity.LEGENDARY));
        loot.add(new LootItem(Material.DRAGON_EGG, 1, 1, LootRarity.LEGENDARY));
        loot.add(new LootItem(Material.BEACON, 1, 1, LootRarity.LEGENDARY));
        
        return loot;
    }
    
    
    // ============================================
    // НАСТРОЙКА КОЛИЧЕСТВА ПРЕДМЕТОВ В СУНДУКЕ
    // ============================================
    
    // Минимальное количество предметов в сундуке
    public static final int MIN_ITEMS_IN_CHEST = 3;
    
    // Максимальное количество предметов в сундуке
    public static final int MAX_ITEMS_IN_CHEST = 8;
}
