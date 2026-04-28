package com.eventManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MessagesConfig {
    private final EventManager plugin;
    private final File configFile;
    private final Gson gson;
    
    // Сообщения
    public String eventStartBorder;
    public String eventStartTitle;
    public String eventStartRarity;
    public String eventStartCoords;
    public String eventStartTimer;
    public String eventStartHint;
    
    public String warning30SecOpen;
    public String warning10SecOpen;
    public String warning5SecOpen;
    
    public String chestOpenBorder;
    public String chestOpenTitle;
    public String chestOpenRarity;
    public String chestOpenAction;
    
    public String warning60SecClose;
    public String warning30SecClose;
    public String warning10SecClose;
    public String warningCountdown;
    
    public String chestDisappeared;
    public String nextEventInfo;
    
    public String protectionTimeLeft;
    public String protectionWait;
    public String protectionCannotBreak;
    public String protectionAutoDisappear;

    public MessagesConfig(EventManager plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        this.configFile = new File(plugin.getDataFolder(), "messages.json");
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            createDefaultConfig();
        }
        
        try (FileReader reader = new FileReader(configFile)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            parseMessages(json);
            plugin.getLogger().info("Сообщения загружены из конфига");
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при чтении messages.json: " + e.getMessage());
            loadDefaultMessages();
        }
    }

    private void createDefaultConfig() {
        plugin.getLogger().info("Создание messages.json...");
        
        JsonObject config = new JsonObject();
        
        // Сообщения о начале события
        JsonObject eventStart = new JsonObject();
        eventStart.addProperty("border", "&6▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        eventStart.addProperty("title", "&e&l⚡ СОБЫТИЕ: СУНДУК С СОКРОВИЩАМИ ⚡");
        eventStart.addProperty("rarity", "&fРедкость лута: {rarity}&l");
        eventStart.addProperty("coords", "&7Координаты: &bX: {x}, Y: {y}, Z: {z}");
        eventStart.addProperty("timer", "&7Открытие через: &c&l1 минуту");
        eventStart.addProperty("hint", "&eПоспешите! Сундук исчезнет через 3 минуты после открытия!");
        config.add("event_start", eventStart);
        
        // Предупреждения до открытия
        JsonObject warningsOpen = new JsonObject();
        warningsOpen.addProperty("30_seconds", "&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⏰ Сундук откроется через &c&l30 секунд&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warningsOpen.addProperty("10_seconds", "&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⏰ Сундук откроется через &c&l10 секунд&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warningsOpen.addProperty("5_seconds", "&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&c&l⏰ ВНИМАНИЕ! Сундук откроется через 5 секунд!\n&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        config.add("warnings_open", warningsOpen);
        
        // Сообщения об открытии
        JsonObject chestOpen = new JsonObject();
        chestOpen.addProperty("border", "&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        chestOpen.addProperty("title", "&a&l✓ СУНДУК ОТКРЫТ!");
        chestOpen.addProperty("rarity", "&fРедкость лута: {rarity}&l");
        chestOpen.addProperty("action", "&eЗабирайте сокровища! У вас есть &c&l2 минуты&e!");
        config.add("chest_open", chestOpen);
        
        // Предупреждения до исчезновения
        JsonObject warningsClose = new JsonObject();
        warningsClose.addProperty("60_seconds", "&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⚠ Сундук исчезнет через &c&l1 минуту&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warningsClose.addProperty("30_seconds", "&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⚠ Сундук исчезнет через &c&l30 секунд&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warningsClose.addProperty("10_seconds", "&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&c&l⚠ ВНИМАНИЕ! Сундук исчезнет через 10 секунд!\n&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warningsClose.addProperty("countdown", "&c&l⚠ {seconds}...");
        config.add("warnings_close", warningsClose);
        
        // Сообщение об исчезновении
        JsonObject disappeared = new JsonObject();
        disappeared.addProperty("border", "&7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        disappeared.addProperty("message", "&7✖ Сундук с сокровищами исчез...");
        disappeared.addProperty("next_event", "&8Следующее событие через 1.5 часа");
        config.add("disappeared", disappeared);
        
        // Сообщения защиты
        JsonObject protection = new JsonObject();
        protection.addProperty("time_left", "&c⏰ Сундук откроется через: &e{time}");
        protection.addProperty("wait", "&7Подождите окончания таймера!");
        protection.addProperty("cannot_break", "&c⛔ Вы не можете сломать этот сундук!");
        protection.addProperty("auto_disappear", "&7Он исчезнет автоматически.");
        config.add("protection", protection);
        
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
            plugin.getLogger().info("Файл messages.json создан");
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при создании messages.json: " + e.getMessage());
        }
    }

    private void parseMessages(JsonObject json) {
        try {
            JsonObject eventStart = json.getAsJsonObject("event_start");
            eventStartBorder = colorize(eventStart.get("border").getAsString());
            eventStartTitle = colorize(eventStart.get("title").getAsString());
            eventStartRarity = colorize(eventStart.get("rarity").getAsString());
            eventStartCoords = colorize(eventStart.get("coords").getAsString());
            eventStartTimer = colorize(eventStart.get("timer").getAsString());
            eventStartHint = colorize(eventStart.get("hint").getAsString());
            
            JsonObject warningsOpen = json.getAsJsonObject("warnings_open");
            warning30SecOpen = colorize(warningsOpen.get("30_seconds").getAsString());
            warning10SecOpen = colorize(warningsOpen.get("10_seconds").getAsString());
            warning5SecOpen = colorize(warningsOpen.get("5_seconds").getAsString());
            
            JsonObject chestOpen = json.getAsJsonObject("chest_open");
            chestOpenBorder = colorize(chestOpen.get("border").getAsString());
            chestOpenTitle = colorize(chestOpen.get("title").getAsString());
            chestOpenRarity = colorize(chestOpen.get("rarity").getAsString());
            chestOpenAction = colorize(chestOpen.get("action").getAsString());
            
            JsonObject warningsClose = json.getAsJsonObject("warnings_close");
            warning60SecClose = colorize(warningsClose.get("60_seconds").getAsString());
            warning30SecClose = colorize(warningsClose.get("30_seconds").getAsString());
            warning10SecClose = colorize(warningsClose.get("10_seconds").getAsString());
            warningCountdown = colorize(warningsClose.get("countdown").getAsString());
            
            JsonObject disappeared = json.getAsJsonObject("disappeared");
            chestDisappeared = colorize(disappeared.get("message").getAsString());
            nextEventInfo = colorize(disappeared.get("next_event").getAsString());
            
            JsonObject protection = json.getAsJsonObject("protection");
            protectionTimeLeft = colorize(protection.get("time_left").getAsString());
            protectionWait = colorize(protection.get("wait").getAsString());
            protectionCannotBreak = colorize(protection.get("cannot_break").getAsString());
            protectionAutoDisappear = colorize(protection.get("auto_disappear").getAsString());
            
        } catch (Exception e) {
            plugin.getLogger().warning("Ошибка при парсинге сообщений: " + e.getMessage());
            loadDefaultMessages();
        }
    }

    private void loadDefaultMessages() {
        // Загружаем дефолтные значения
        eventStartBorder = colorize("&6▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        eventStartTitle = colorize("&e&l⚡ СОБЫТИЕ: СУНДУК С СОКРОВИЩАМИ ⚡");
        eventStartRarity = colorize("&fРедкость лута: {rarity}&l");
        eventStartCoords = colorize("&7Координаты: &bX: {x}, Y: {y}, Z: {z}");
        eventStartTimer = colorize("&7Открытие через: &c&l1 минуту");
        eventStartHint = colorize("&eПоспешите! Сундук исчезнет через 3 минуты после открытия!");
        
        warning30SecOpen = colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⏰ Сундук откроется через &c&l30 секунд&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warning10SecOpen = colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⏰ Сундук откроется через &c&l10 секунд&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warning5SecOpen = colorize("&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&c&l⏰ ВНИМАНИЕ! Сундук откроется через 5 секунд!\n&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        chestOpenBorder = colorize("&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        chestOpenTitle = colorize("&a&l✓ СУНДУК ОТКРЫТ!");
        chestOpenRarity = colorize("&fРедкость лута: {rarity}&l");
        chestOpenAction = colorize("&eЗабирайте сокровища! У вас есть &c&l2 минуты&e!");
        
        warning60SecClose = colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⚠ Сундук исчезнет через &c&l1 минуту&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warning30SecClose = colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&e⚠ Сундук исчезнет через &c&l30 секунд&e!\n&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warning10SecClose = colorize("&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n&c&l⚠ ВНИМАНИЕ! Сундук исчезнет через 10 секунд!\n&c━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        warningCountdown = colorize("&c&l⚠ {seconds}...");
        
        chestDisappeared = colorize("&7✖ Сундук с сокровищами исчез...");
        nextEventInfo = colorize("&8Следующее событие через 1.5 часа");
        
        protectionTimeLeft = colorize("&c⏰ Сундук откроется через: &e{time}");
        protectionWait = colorize("&7Подождите окончания таймера!");
        protectionCannotBreak = colorize("&c⛔ Вы не можете сломать этот сундук!");
        protectionAutoDisappear = colorize("&7Он исчезнет автоматически.");
    }

    private String colorize(String text) {
        return text.replace("&", "§");
    }

    public void reloadConfig() {
        loadConfig();
    }
}
