package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.config.DungeonConfig;

import java.io.IOException;
import java.util.*;

/**
 * Generates an entire Dungeon structure based on configuration.
 * <p>
 * Creates all floors, generates rooms for each floor, connects rooms,
 * and builds a global room map for fast lookups.
 */
public class DungeonGenerator {
    private final DungeonConfig dungeonConfig;
    private final FloorGenerator floorGenerator;
    private final RoomConnector roomConnector;

    public DungeonGenerator(DungeonConfig dungeonConfig) throws IOException {
        this.dungeonConfig = dungeonConfig;
        Random random = new Random();
        floorGenerator = new FloorGenerator(dungeonConfig, random);
        this.roomConnector = new RoomConnector(random);
    }

    public Dungeon generateDungeon(){
        Dungeon dungeon = new Dungeon();
        List<Floor> floors = new ArrayList<>();
        Map<String, Room> roomMap = new HashMap<>();

        for(int floorNumber = 1; floorNumber <= dungeonConfig.getFloors(); floorNumber++){
            Floor floor = floorGenerator.generateFloor(floorNumber, roomMap);
            roomConnector.connectRooms(floor, roomMap);
            floors.add(floor);
        }
        dungeon.setFloors(floors);
        dungeon.setRoomById(roomMap);
        return dungeon;
    }
}
