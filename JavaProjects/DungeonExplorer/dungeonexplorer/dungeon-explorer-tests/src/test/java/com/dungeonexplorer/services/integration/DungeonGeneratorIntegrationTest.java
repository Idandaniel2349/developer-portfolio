package com.dungeonexplorer.services.integration;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.generators.DungeonGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class DungeonGeneratorIntegrationTest {

    @Test
    public void generateDungeon_success() throws IOException {
        DungeonConfig config = new DungeonConfig();
        config.setFloors(2);
        config.setMinRoomsPerFloor(2);
        config.setMaxRoomsPerFloor(3);
        config.setMinEnemiesPerRoom(1);
        config.setMaxEnemiesPerRoom(2);
        config.setMinLootPerRoom(1);
        config.setMaxLootPerRoom(2);
        config.setEnemySpawnChance(1.0);
        config.setLootSpawnChance(1.0);
        config.setLootQuantity(1);

        DungeonGenerator generator = new DungeonGenerator(config);
        Dungeon dungeon = generator.generateDungeon();

        List<Floor> floors = dungeon.getFloors();
        assertEquals(2, floors.size(), "Dungeon should have 2 floors");

        Map<String, Room> roomMap = dungeon.getRoomById();
        assertNotNull(roomMap);
        assertFalse(roomMap.isEmpty(), "Dungeon should have rooms");

        int numOfBossRooms = 0;
        for (Floor floor : floors) {
            assertNotNull(floor.getStartingRoomId());
            assertNotNull(floor.getExitRoomId());
            List<String> roomsIds = floor.getRoomIds();
            assertTrue(roomsIds.size() >= 2 && roomsIds.size() <= 3, "Rooms count per floor should match config");

            for (String roomId : roomsIds) {
                Room room = dungeon.getRoomById().get(roomId);
                if(room.isBossRoom()){
                    numOfBossRooms++;
                }
                assertNotNull(room.getLoot());
                assertFalse(room.getEnemies().isEmpty());
            }
        }
        assertEquals(floors.size(), numOfBossRooms);
    }
}
