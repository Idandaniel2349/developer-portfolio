package com.dungeonexplorer.services.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties
public class DungeonConfig {
    private int floors;
    private int minRoomsPerFloor;
    private int maxRoomsPerFloor;
    private int minEnemiesPerRoom;
    private int maxEnemiesPerRoom;
    private int minLootPerRoom;
    private int maxLootPerRoom;
    private int lootQuantity;
    private double enemySpawnChance; // probability a room has enemies
    private double lootSpawnChance;  // probability a room has loot
}
