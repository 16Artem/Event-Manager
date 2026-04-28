package com.eventManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConfigManager {
    private final EventManager plugin;
    private final File configFile;
    private final Gson gson;
    private List<EventLocation> locations;

    public ConfigManager(EventManager plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Создаем папку плагина если её нет
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        this.configFile = new File(plugin.getDataFolder(), "locations.json");
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            createDefaultConfig();
        }
        
        try (FileReader reader = new FileReader(configFile)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            locations = parseLocations(json);
            plugin.getLogger().info("Загружено локаций: " + locations.size());
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при чтении конфига: " + e.getMessage());
            locations = getDefaultLocations();
        }
    }

    private void createDefaultConfig() {
        plugin.getLogger().info("Создание конфигурационного файла по умолчанию...");
        
        JsonObject config = new JsonObject();
        JsonArray locationsArray = new JsonArray();
        
        // Добавляем примеры локаций
        locationsArray.add(createLocationJson("world", 100, 64, 100));
        locationsArray.add(createLocationJson("world", -50, 70, 200));
        locationsArray.add(createLocationJson("world", 300, 65, -150));
        locationsArray.add(createLocationJson("world", -200, 68, -300));
        locationsArray.add(createLocationJson("world", 0, 75, 0));
        
        config.add("locations", locationsArray);
        
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
            plugin.getLogger().info("Конфигурационный файл создан: " + configFile.getPath());
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при создании конфига: " + e.getMessage());
        }
    }

    private JsonObject createLocationJson(String world, double x, double y, double z) {
        JsonObject location = new JsonObject();
        location.addProperty("world", world);
        location.addProperty("x", x);
        location.addProperty("y", y);
        location.addProperty("z", z);
        return location;
    }

    private List<EventLocation> parseLocations(JsonObject json) {
        List<EventLocation> result = new ArrayList<>();
        
        if (!json.has("locations")) {
            plugin.getLogger().warning("В конфиге отсутствует массив 'locations'!");
            return getDefaultLocations();
        }
        
        JsonArray locationsArray = json.getAsJsonArray("locations");
        
        for (int i = 0; i < locationsArray.size(); i++) {
            try {
                JsonObject locObj = locationsArray.get(i).getAsJsonObject();
                
                String world = locObj.get("world").getAsString();
                double x = locObj.get("x").getAsDouble();
                double y = locObj.get("y").getAsDouble();
                double z = locObj.get("z").getAsDouble();
                
                result.add(new EventLocation(world, x, y, z));
            } catch (Exception e) {
                plugin.getLogger().warning("Ошибка при парсинге локации #" + i + ": " + e.getMessage());
            }
        }
        
        if (result.isEmpty()) {
            plugin.getLogger().warning("Не удалось загрузить локации из конфига! Используются стандартные.");
            return getDefaultLocations();
        }
        
        return result;
    }

    private List<EventLocation> getDefaultLocations() {
        List<EventLocation> defaults = new ArrayList<>();
        defaults.add(new EventLocation("world", 100, 64, 100));
        defaults.add(new EventLocation("world", -50, 70, 200));
        defaults.add(new EventLocation("world", 300, 65, -150));
        defaults.add(new EventLocation("world", -200, 68, -300));
        defaults.add(new EventLocation("world", 0, 75, 0));
        return defaults;
    }

    public List<EventLocation> getLocations() {
        return locations;
    }

    public void reloadConfig() {
        loadConfig();
    }

    public void addLocation(EventLocation location) {
        locations.add(location);
        saveConfig();
    }

    public void removeLocation(int index) {
        if (index >= 0 && index < locations.size()) {
            locations.remove(index);
            saveConfig();
        }
    }

    private void saveConfig() {
        JsonObject config = new JsonObject();
        JsonArray locationsArray = new JsonArray();
        
        for (EventLocation loc : locations) {
            locationsArray.add(createLocationJson(
                loc.getWorldName(),
                loc.getX(),
                loc.getY(),
                loc.getZ()
            ));
        }
        
        config.add("locations", locationsArray);
        
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при сохранении конфига: " + e.getMessage());
        }
    }
}
