package com.eventManager;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class responsible for initialization and lifecycle management.
 * Coordinates configuration loading, event scheduling, and listener registration.
 */
public final class EventManager extends JavaPlugin {
    
    private EventScheduler eventScheduler;
    private ChestProtectionListener protectionListener;
    private ConfigManager configManager;
    private MessagesConfig messagesConfig;
    private LootConfigManager lootConfigManager;

    @Override
    public void onEnable() {
        getLogger().info("=================================");
        getLogger().info("  EventManager загружается...  ");
        getLogger().info("=================================");

        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        messagesConfig = new MessagesConfig(this);
        messagesConfig.loadConfig();
        
        lootConfigManager = new LootConfigManager(this);
        lootConfigManager.loadConfig();

        eventScheduler = new EventScheduler(this);

        protectionListener = new ChestProtectionListener(eventScheduler, messagesConfig);
        getServer().getPluginManager().registerEvents(protectionListener, this);

        getCommand("event").setExecutor(new EventCommand(this));
        getCommand("event").setTabCompleter(new EventCommand(this));

        getServer().getScheduler().runTaskLater(this, () -> {
            eventScheduler.start();
            getLogger().info("Планировщик событий запущен!");
            getLogger().info("Интервал событий: " + LootConfig.MIN_EVENT_INTERVAL + "-" + 
                           LootConfig.MAX_EVENT_INTERVAL + " секунд (" + 
                           (LootConfig.MIN_EVENT_INTERVAL / 3600.0) + " часа)");
            getLogger().info("Доступно локаций: " + configManager.getLocations().size());
        }, 100L);

        getLogger().info("=================================");
        getLogger().info("  EventManager успешно загружен! ");
        getLogger().info("=================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("Остановка EventManager...");

        if (eventScheduler != null) {
            eventScheduler.stop();
        }

        getLogger().info("EventManager остановлен!");
    }

    public EventScheduler getEventScheduler() {
        return eventScheduler;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public LootConfigManager getLootConfigManager() {
        return lootConfigManager;
    }
}
