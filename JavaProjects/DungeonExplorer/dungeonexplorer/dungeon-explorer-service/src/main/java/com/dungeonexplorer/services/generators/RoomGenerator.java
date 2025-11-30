package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.config.DungeonConfig;

import com.dungeonexplorer.services.templates.TemplateRegistry;

import java.io.IOException;
import java.util.Random;

/**
 * Creates individual rooms with IDs, names, default descriptions,
 * and populates them with enemies and loot.
 * <p>
 * Handles boss room logic and random generation of room content.
 */
public class RoomGenerator {

    private final DungeonConfig dungeonConfig;
    private final Random random;
    private final TemplateRegistry templateRegistry;

    private final EnemyGenerator enemyGenerator;
    private final LootGenerator lootGenerator;

    public RoomGenerator(DungeonConfig dungeonConfig, Random random) throws IOException {
        this.dungeonConfig = dungeonConfig;
        this.random = random;
        this.templateRegistry = new TemplateRegistry();
        enemyGenerator = new EnemyGenerator(random, dungeonConfig, templateRegistry);
        lootGenerator = new LootGenerator(dungeonConfig, random, templateRegistry);
    }

    public Room generateRoom(int floorNumber, int roomNumber, boolean isBoss){
        String roomId = "floor" + floorNumber +"_room" + roomNumber;
        String roomName = "Room" + roomNumber;
        String description = "A dungeon Room";
        Room room = new Room(roomId, roomName, description);

        // add enemies/ loot
        populateRoom(room, floorNumber, isBoss);

        return room;
    }

    private void populateRoom(Room room, int floorNumber, boolean isBoss){
        // handle loot
        int minLoot = dungeonConfig.getMinLootPerRoom();
        int maxLoot = dungeonConfig.getMaxLootPerRoom();
        int numLoot = random.nextInt((maxLoot- minLoot) + 1) + minLoot;
        room.setLoot(lootGenerator.generateLoot(numLoot));

        // handle enemies
        int minEnemies = dungeonConfig.getMinEnemiesPerRoom();
        int maxEnemies = dungeonConfig.getMaxEnemiesPerRoom();
        int numEnemies = random.nextInt((maxEnemies- minEnemies) + 1) + minEnemies;
        if(isBoss){
            room.setEnemies(enemyGenerator.generateEnemies(1,floorNumber, true));
            room.setBossRoom(true);
        }else {
            room.setEnemies(enemyGenerator.generateEnemies(numEnemies,floorNumber, false));
        }
    }
}
