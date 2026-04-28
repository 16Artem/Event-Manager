package com.eventManager;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestProtectionListener implements Listener {
    private final EventScheduler eventScheduler;
    private final MessagesConfig messages;

    public ChestProtectionListener(EventScheduler eventScheduler, MessagesConfig messages) {
        this.eventScheduler = eventScheduler;
        this.messages = messages;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Player player = event.getPlayer();

        // Проверяем, является ли этот блок сундуком события
        for (LootChestEvent chestEvent : eventScheduler.getActiveEvents()) {
            if (chestEvent.getLocation().getBlock().equals(block)) {
                if (!chestEvent.isOpen()) {
                    // Сундук еще не открыт - блокируем
                    event.setCancelled(true);
                    
                    int timeLeft = chestEvent.getTimeLeft();
                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;
                    String timeStr = String.format("%d:%02d", minutes, seconds);
                    
                    player.sendMessage(messages.protectionTimeLeft.replace("{time}", timeStr));
                    player.sendMessage(messages.protectionWait);
                }
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getPlayer();
        Block block = event.getInventory().getLocation().getBlock();

        // Проверяем, является ли этот сундук событием
        for (LootChestEvent chestEvent : eventScheduler.getActiveEvents()) {
            if (chestEvent.getLocation().getBlock().equals(block)) {
                if (!chestEvent.isOpen()) {
                    // Сундук еще не открыт - блокируем
                    event.setCancelled(true);
                    
                    int timeLeft = chestEvent.getTimeLeft();
                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;
                    String timeStr = String.format("%d:%02d", minutes, seconds);
                    
                    player.sendMessage(messages.protectionTimeLeft.replace("{time}", timeStr));
                }
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // Проверяем, является ли этот блок сундуком события
        for (LootChestEvent chestEvent : eventScheduler.getActiveEvents()) {
            if (chestEvent.getLocation().getBlock().equals(block)) {
                // Блокируем разрушение сундука события
                event.setCancelled(true);
                
                if (!chestEvent.isOpen()) {
                    int timeLeft = chestEvent.getTimeLeft();
                    int minutes = timeLeft / 60;
                    int seconds = timeLeft % 60;
                    String timeStr = String.format("%d:%02d", minutes, seconds);
                    
                    player.sendMessage(messages.protectionCannotBreak);
                    player.sendMessage(messages.protectionTimeLeft.replace("{time}", timeStr));
                } else {
                    player.sendMessage(messages.protectionCannotBreak);
                    player.sendMessage(messages.protectionAutoDisappear);
                }
                return;
            }
        }
    }
}
