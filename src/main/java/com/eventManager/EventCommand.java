package com.eventManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {
    private final EventManager plugin;

    public EventCommand(EventManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("eventmanager.admin")) {
            sender.sendMessage(ChatColor.RED + "У вас нет прав для использования этой команды!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                plugin.getEventScheduler().forceSpawnEvent();
                sender.sendMessage(ChatColor.GREEN + "Событие принудительно запущено!");
                break;

            case "stop":
                plugin.getEventScheduler().stop();
                sender.sendMessage(ChatColor.YELLOW + "Все события остановлены!");
                break;

            case "restart":
                plugin.getEventScheduler().stop();
                plugin.getEventScheduler().start();
                sender.sendMessage(ChatColor.GREEN + "Планировщик событий перезапущен!");
                break;

            case "info":
                sendInfo(sender);
                break;

            case "reload":
                sender.sendMessage(ChatColor.YELLOW + "Перезагрузка плагина...");
                plugin.getEventScheduler().stop();
                plugin.getConfigManager().reloadConfig();
                plugin.getMessagesConfig().reloadConfig();
                plugin.getLootConfigManager().reloadConfig();
                plugin.getEventScheduler().start();
                sender.sendMessage(ChatColor.GREEN + "Плагин перезагружен!");
                sender.sendMessage(ChatColor.GRAY + "Загружено локаций: " + plugin.getConfigManager().getLocations().size());
                break;

            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== EventManager Команды ===");
        sender.sendMessage(ChatColor.YELLOW + "/event start" + ChatColor.WHITE + " - Запустить событие немедленно");
        sender.sendMessage(ChatColor.YELLOW + "/event stop" + ChatColor.WHITE + " - Остановить все события");
        sender.sendMessage(ChatColor.YELLOW + "/event restart" + ChatColor.WHITE + " - Перезапустить планировщик");
        sender.sendMessage(ChatColor.YELLOW + "/event info" + ChatColor.WHITE + " - Информация о настройках");
        sender.sendMessage(ChatColor.YELLOW + "/event reload" + ChatColor.WHITE + " - Перезагрузить плагин");
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Информация о событиях ===");
        
        int minInterval = LootConfig.MIN_EVENT_INTERVAL;
        int maxInterval = LootConfig.MAX_EVENT_INTERVAL;
        int minHours = minInterval / 3600;
        int minMinutes = (minInterval % 3600) / 60;
        int maxHours = maxInterval / 3600;
        int maxMinutes = (maxInterval % 3600) / 60;
        
        sender.sendMessage(ChatColor.YELLOW + "Интервал событий: " + ChatColor.WHITE + 
                         minHours + "ч " + minMinutes + "м - " + maxHours + "ч " + maxMinutes + "м");
        sender.sendMessage(ChatColor.YELLOW + "Время до открытия: " + ChatColor.WHITE + 
                         LootConfig.CHEST_OPEN_DELAY + " секунд");
        sender.sendMessage(ChatColor.YELLOW + "Время жизни сундука: " + ChatColor.WHITE + 
                         LootConfig.CHEST_LIFETIME + " секунд");
        sender.sendMessage(ChatColor.YELLOW + "Количество локаций: " + ChatColor.WHITE + 
                         plugin.getConfigManager().getLocations().size());
        sender.sendMessage(ChatColor.YELLOW + "Активных событий: " + ChatColor.WHITE + 
                         plugin.getEventScheduler().getActiveEvents().size());
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("start");
            completions.add("stop");
            completions.add("restart");
            completions.add("info");
            completions.add("reload");
        }

        return completions;
    }
}
