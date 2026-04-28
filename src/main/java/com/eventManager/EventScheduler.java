package com.eventManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventScheduler {
    private final EventManager plugin;
    private final List<LootChestEvent> activeEvents;
    private BukkitRunnable schedulerTask;

    public EventScheduler(EventManager plugin) {
        this.plugin = plugin;
        this.activeEvents = new ArrayList<>();
    }

    public void start() {
        scheduleNextEvent();
    }

    public void stop() {
        if (schedulerTask != null) {
            schedulerTask.cancel();
        }

        // Удаляем все активные события
        for (LootChestEvent event : new ArrayList<>(activeEvents)) {
            event.remove();
        }
        activeEvents.clear();
    }

    private void scheduleNextEvent() {
        Random random = new Random();
        
        // Вычисляем случайное время до следующего события
        int minInterval = LootConfig.MIN_EVENT_INTERVAL;
        int maxInterval = LootConfig.MAX_EVENT_INTERVAL;
        int delay = minInterval + random.nextInt(maxInterval - minInterval + 1);

        schedulerTask = new BukkitRunnable() {
            @Override
            public void run() {
                spawnEvent();
                scheduleNextEvent(); // Планируем следующее событие
            }
        };

        schedulerTask.runTaskLater(plugin, delay * 20L); // Конвертируем секунды в тики

        // Логирование
        int hours = delay / 3600;
        int minutes = (delay % 3600) / 60;
        plugin.getLogger().info("Следующее событие запланировано через " + delay + " секунд (" + 
                              hours + " ч " + minutes + " мин)");
    }

    private void spawnEvent() {
        List<EventLocation> locations = plugin.getConfigManager().getLocations();
        
        if (locations.isEmpty()) {
            plugin.getLogger().warning("Нет доступных локаций для событий!");
            return;
        }

        // Выбираем случайную локацию
        Random random = new Random();
        EventLocation eventLocation = locations.get(random.nextInt(locations.size()));

        // Получаем мир
        World world = Bukkit.getWorld(eventLocation.getWorldName());
        if (world == null) {
            plugin.getLogger().warning("Мир " + eventLocation.getWorldName() + " не найден!");
            return;
        }

        // Создаем локацию
        Location location = eventLocation.toLocation(world);

        // Создаем и запускаем событие
        LootChestEvent event = new LootChestEvent(plugin, location);
        activeEvents.add(event);
        event.start();

        plugin.getLogger().info("Событие создано на координатах: " + 
                              String.format("X: %.0f, Y: %.0f, Z: %.0f в мире %s", 
                              location.getX(), location.getY(), location.getZ(), world.getName()));

        // Удаляем событие из списка после его завершения
        new BukkitRunnable() {
            @Override
            public void run() {
                activeEvents.remove(event);
            }
        }.runTaskLater(plugin, (LootConfig.CHEST_OPEN_DELAY + LootConfig.CHEST_LIFETIME) * 20L);
    }

    public List<LootChestEvent> getActiveEvents() {
        return new ArrayList<>(activeEvents);
    }

    public void forceSpawnEvent() {
        spawnEvent();
    }
}
