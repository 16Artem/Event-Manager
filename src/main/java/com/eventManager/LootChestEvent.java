package com.eventManager;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LootChestEvent {
    private final EventManager plugin;
    private final Location location;
    private final MessagesConfig messages;
    private final LootConfigManager lootConfig;
    private Block chestBlock;
    private ArmorStand hologram;
    private boolean isOpen = false;
    private int timeLeft;
    private LootRarity preSelectedRarity;

    public LootChestEvent(EventManager plugin, Location location) {
        this.plugin = plugin;
        this.location = location;
        this.messages = plugin.getMessagesConfig();
        this.lootConfig = plugin.getLootConfigManager();
        this.timeLeft = LootConfig.CHEST_OPEN_DELAY;
    }

    public void start() {
        chestBlock = location.getBlock();
        chestBlock.setType(Material.CHEST);
        createHologram();
        spawnEffects();
        announceEvent();
        startTimer();
    }

    private void createHologram() {
        Location holoLoc = location.clone().add(0.5, 1.5, 0.5);
        hologram = (ArmorStand) location.getWorld().spawnEntity(holoLoc, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setCustomNameVisible(true);
        hologram.setMarker(true);
        hologram.setInvulnerable(true);
        updateHologramText();
    }

    private void updateHologramText() {
        if (hologram != null && !hologram.isDead()) {
            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;
            hologram.setCustomName(ChatColor.YELLOW + "⏰ Открытие через: " + 
                                  ChatColor.WHITE + String.format("%d:%02d", minutes, seconds));
        }
    }

    private void removeHologram() {
        if (hologram != null && !hologram.isDead()) {
            hologram.remove();
            hologram = null;
        }
    }

    private void spawnEffects() {
        World world = location.getWorld();
        if (world == null) return;

        world.spawnParticle(Particle.FIREWORK, location.clone().add(0.5, 1, 0.5), 50, 0.5, 0.5, 0.5, 0.1);
        world.spawnParticle(Particle.END_ROD, location.clone().add(0.5, 1, 0.5), 30, 0.3, 0.5, 0.3, 0.05);
        world.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

    private void announceEvent() {
        preSelectedRarity = selectEventRarity();
        
        String rarityText = messages.eventStartRarity.replace("{rarity}", preSelectedRarity.getColoredName());
        String coordsText = messages.eventStartCoords
            .replace("{x}", String.format("%.0f", location.getX()))
            .replace("{y}", String.format("%.0f", location.getY()))
            .replace("{z}", String.format("%.0f", location.getZ()));
        
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(messages.eventStartBorder);
        Bukkit.broadcastMessage(messages.eventStartTitle);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(rarityText);
        Bukkit.broadcastMessage(coordsText);
        Bukkit.broadcastMessage(messages.eventStartTimer);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(messages.eventStartHint);
        Bukkit.broadcastMessage(messages.eventStartBorder);
        Bukkit.broadcastMessage("");
        
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 2.0f);
        }
    }

    private void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateHologramText();

                    if (timeLeft % 10 == 0) {
                        World world = location.getWorld();
                        if (world != null) {
                            world.spawnParticle(Particle.ENCHANT, location.clone().add(0.5, 1, 0.5), 20, 0.3, 0.5, 0.3, 0.1);
                        }
                    }

                    if (timeLeft == 30) {
                        Bukkit.broadcastMessage("");
                        for (String line : messages.warning30SecOpen.split("\\n")) {
                            Bukkit.broadcastMessage(line);
                        }
                        Bukkit.broadcastMessage("");
                    } else if (timeLeft == 10) {
                        Bukkit.broadcastMessage("");
                        for (String line : messages.warning10SecOpen.split("\\n")) {
                            Bukkit.broadcastMessage(line);
                        }
                        Bukkit.broadcastMessage("");
                    } else if (timeLeft == 5) {
                        Bukkit.broadcastMessage("");
                        for (String line : messages.warning5SecOpen.split("\\n")) {
                            Bukkit.broadcastMessage(line);
                        }
                        Bukkit.broadcastMessage("");
                    }
                } else {
                    openChest();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void openChest() {
        isOpen = true;
        removeHologram();
        fillChestWithLoot();

        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.EXPLOSION, location.clone().add(0.5, 1, 0.5), 5, 0.3, 0.3, 0.3, 0.1);
            world.spawnParticle(Particle.TOTEM_OF_UNDYING, location.clone().add(0.5, 1, 0.5), 100, 0.5, 0.5, 0.5, 0.2);
            world.playSound(location, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.5f);
            world.playSound(location, Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
            world.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        }

        String rarityText = messages.chestOpenRarity.replace("{rarity}", preSelectedRarity.getColoredName());
        
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(messages.chestOpenBorder);
        Bukkit.broadcastMessage(messages.chestOpenTitle);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(rarityText);
        Bukkit.broadcastMessage(messages.chestOpenAction);
        Bukkit.broadcastMessage(messages.chestOpenBorder);
        Bukkit.broadcastMessage("");

        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.2f);
        }

        new BukkitRunnable() {
            int countdown = LootConfig.CHEST_LIFETIME;
            
            @Override
            public void run() {
                countdown--;
                
                if (countdown == 60) {
                    Bukkit.broadcastMessage("");
                    for (String line : messages.warning60SecClose.split("\\n")) {
                        Bukkit.broadcastMessage(line);
                    }
                    Bukkit.broadcastMessage("");
                } else if (countdown == 30) {
                    Bukkit.broadcastMessage("");
                    for (String line : messages.warning30SecClose.split("\\n")) {
                        Bukkit.broadcastMessage(line);
                    }
                    Bukkit.broadcastMessage("");
                } else if (countdown == 10) {
                    Bukkit.broadcastMessage("");
                    for (String line : messages.warning10SecClose.split("\\n")) {
                        Bukkit.broadcastMessage(line);
                    }
                    Bukkit.broadcastMessage("");
                } else if (countdown <= 5 && countdown > 0) {
                    Bukkit.broadcastMessage(messages.warningCountdown.replace("{seconds}", String.valueOf(countdown)));
                }
                
                if (countdown <= 0) {
                    remove();
                    
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(messages.eventStartBorder.replace("&6", "&7"));
                    Bukkit.broadcastMessage(messages.chestDisappeared);
                    Bukkit.broadcastMessage(messages.nextEventInfo);
                    Bukkit.broadcastMessage(messages.eventStartBorder.replace("&6", "&7"));
                    Bukkit.broadcastMessage("");
                    
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void fillChestWithLoot() {
        if (!(chestBlock.getState() instanceof Chest)) return;

        Chest chest = (Chest) chestBlock.getState();
        Inventory inventory = chest.getInventory();
        inventory.clear();

        List<LootItem> availableLoot = getLootByRarity(preSelectedRarity);
        Random random = new Random();
        int itemCount = LootConfig.MIN_ITEMS_IN_CHEST + 
                       random.nextInt(LootConfig.MAX_ITEMS_IN_CHEST - LootConfig.MIN_ITEMS_IN_CHEST + 1);

        List<Integer> slots = getRandomSlots(inventory.getSize(), itemCount);

        for (int i = 0; i < itemCount && i < availableLoot.size(); i++) {
            LootItem lootItem = availableLoot.get(random.nextInt(availableLoot.size()));
            int amount = lootItem.getMinAmount() + 
                        random.nextInt(lootItem.getMaxAmount() - lootItem.getMinAmount() + 1);
            
            ItemStack item = lootItem.createItemStack(amount);
            inventory.setItem(slots.get(i), item);
        }
    }

    private LootRarity selectEventRarity() {
        Random random = new Random();
        double totalWeight = 0;
        
        for (LootRarity rarity : LootRarity.values()) {
            totalWeight += rarity.getWeight();
        }

        double randomValue = random.nextDouble() * totalWeight;
        double currentWeight = 0;

        for (LootRarity rarity : LootRarity.values()) {
            currentWeight += rarity.getWeight();
            if (randomValue <= currentWeight) {
                return rarity;
            }
        }

        return LootRarity.COMMON;
    }

    private List<LootItem> getLootByRarity(LootRarity rarity) {
        return switch (rarity) {
            case COMMON -> lootConfig.getCommonLoot();
            case RARE -> lootConfig.getRareLoot();
            case EPIC -> lootConfig.getEpicLoot();
            case LEGENDARY -> lootConfig.getLegendaryLoot();
        };
    }

    private List<Integer> getRandomSlots(int inventorySize, int count) {
        List<Integer> allSlots = new ArrayList<>();
        for (int i = 0; i < inventorySize; i++) {
            allSlots.add(i);
        }
        Collections.shuffle(allSlots);
        return allSlots.subList(0, Math.min(count, allSlots.size()));
    }

    public void remove() {
        removeHologram();

        if (chestBlock != null && chestBlock.getType() == Material.CHEST) {
            chestBlock.setType(Material.AIR);
        }

        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.SMOKE, location.clone().add(0.5, 1, 0.5), 30, 0.3, 0.5, 0.3, 0.05);
            world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 1.0f);
        }
    }

    public Location getLocation() {
        return location;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
