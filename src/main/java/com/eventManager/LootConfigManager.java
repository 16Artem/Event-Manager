package com.eventManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LootConfigManager {
    private final EventManager plugin;
    private final File configFile;
    private final Gson gson;
    
    private List<LootItem> commonLoot;
    private List<LootItem> rareLoot;
    private List<LootItem> epicLoot;
    private List<LootItem> legendaryLoot;

    public LootConfigManager(EventManager plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        this.configFile = new File(plugin.getDataFolder(), "loot.json");
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            createDefaultConfig();
        }
        
        try (FileReader reader = new FileReader(configFile)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            parseLoot(json);
            plugin.getLogger().info("Лут загружен из конфига");
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при чтении loot.json: " + e.getMessage());
            loadDefaultLoot();
        }
    }

    private void createDefaultConfig() {
        plugin.getLogger().info("Создание loot.json...");
        
        JsonObject config = new JsonObject();
        
        // Обычный лут
        JsonArray commonArray = new JsonArray();
        commonArray.add(createLootJson("IRON_INGOT", 1, 5));
        commonArray.add(createLootJson("COAL", 5, 15));
        commonArray.add(createLootJson("BREAD", 3, 10));
        commonArray.add(createLootJson("ARROW", 10, 32));
        commonArray.add(createLootJson("COBBLESTONE", 10, 32));
        commonArray.add(createLootJson("OAK_LOG", 5, 16));
        commonArray.add(createLootJson("LEATHER", 2, 8));
        config.add("common", commonArray);
        
        // Редкий лут
        JsonArray rareArray = new JsonArray();
        rareArray.add(createLootJson("GOLD_INGOT", 1, 5));
        rareArray.add(createLootJson("REDSTONE", 5, 15));
        rareArray.add(createLootJson("LAPIS_LAZULI", 3, 10));
        rareArray.add(createLootJson("ENDER_PEARL", 1, 3));
        rareArray.add(createLootJson("IRON_SWORD", 1, 1));
        rareArray.add(createLootJson("IRON_CHESTPLATE", 1, 1));
        rareArray.add(createLootJson("GOLDEN_APPLE", 1, 3));
        rareArray.add(createLootJson("EXPERIENCE_BOTTLE", 5, 15));
        config.add("rare", rareArray);
        
        // Эпический лут
        JsonArray epicArray = new JsonArray();
        epicArray.add(createLootJson("DIAMOND", 1, 5));
        epicArray.add(createLootJson("EMERALD", 1, 3));
        epicArray.add(createLootJson("DIAMOND_SWORD", 1, 1));
        epicArray.add(createLootJson("DIAMOND_PICKAXE", 1, 1));
        epicArray.add(createLootJson("DIAMOND_CHESTPLATE", 1, 1));
        epicArray.add(createLootJson("ENCHANTED_GOLDEN_APPLE", 1, 2));
        epicArray.add(createLootJson("TOTEM_OF_UNDYING", 1, 1));
        epicArray.add(createLootJson("ELYTRA", 1, 1));
        config.add("epic", epicArray);
        
        // Легендарный лут
        JsonArray legendaryArray = new JsonArray();
        legendaryArray.add(createLootJson("NETHERITE_INGOT", 1, 3));
        legendaryArray.add(createLootJson("NETHERITE_SWORD", 1, 1));
        legendaryArray.add(createLootJson("NETHERITE_CHESTPLATE", 1, 1));
        legendaryArray.add(createLootJson("NETHERITE_PICKAXE", 1, 1));
        legendaryArray.add(createLootJson("ENCHANTED_GOLDEN_APPLE", 2, 5));
        legendaryArray.add(createLootJson("NETHER_STAR", 1, 2));
        legendaryArray.add(createLootJson("DRAGON_EGG", 1, 1));
        legendaryArray.add(createLootJson("BEACON", 1, 1));
        config.add("legendary", legendaryArray);
        
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
            plugin.getLogger().info("Файл loot.json создан");
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при создании loot.json: " + e.getMessage());
        }
    }

    private JsonObject createLootJson(String material, int min, int max) {
        JsonObject item = new JsonObject();
        item.addProperty("material", material);
        item.addProperty("min_amount", min);
        item.addProperty("max_amount", max);
        return item;
    }

    private void parseLoot(JsonObject json) {
        try {
            commonLoot = parseLootArray(json.getAsJsonArray("common"), LootRarity.COMMON);
            rareLoot = parseLootArray(json.getAsJsonArray("rare"), LootRarity.RARE);
            epicLoot = parseLootArray(json.getAsJsonArray("epic"), LootRarity.EPIC);
            legendaryLoot = parseLootArray(json.getAsJsonArray("legendary"), LootRarity.LEGENDARY);
        } catch (Exception e) {
            plugin.getLogger().warning("Ошибка при парсинге лута: " + e.getMessage());
            loadDefaultLoot();
        }
    }

    private List<LootItem> parseLootArray(JsonArray array, LootRarity rarity) {
        List<LootItem> items = new ArrayList<>();
        
        for (int i = 0; i < array.size(); i++) {
            try {
                JsonObject itemObj = array.get(i).getAsJsonObject();
                
                String materialName = itemObj.get("material").getAsString();
                int minAmount = itemObj.get("min_amount").getAsInt();
                int maxAmount = itemObj.get("max_amount").getAsInt();
                
                Material material = Material.getMaterial(materialName);
                if (material == null) {
                    plugin.getLogger().warning("Неизвестный материал: " + materialName);
                    continue;
                }
                
                items.add(new LootItem(material, minAmount, maxAmount, rarity));
            } catch (Exception e) {
                plugin.getLogger().warning("Ошибка при парсинге предмета #" + i + ": " + e.getMessage());
            }
        }
        
        return items;
    }

    private void loadDefaultLoot() {
        commonLoot = LootConfig.getCommonLoot();
        rareLoot = LootConfig.getRareLoot();
        epicLoot = LootConfig.getEpicLoot();
        legendaryLoot = LootConfig.getLegendaryLoot();
    }

    public List<LootItem> getCommonLoot() {
        return commonLoot;
    }

    public List<LootItem> getRareLoot() {
        return rareLoot;
    }

    public List<LootItem> getEpicLoot() {
        return epicLoot;
    }

    public List<LootItem> getLegendaryLoot() {
        return legendaryLoot;
    }

    public void reloadConfig() {
        loadConfig();
    }
}
