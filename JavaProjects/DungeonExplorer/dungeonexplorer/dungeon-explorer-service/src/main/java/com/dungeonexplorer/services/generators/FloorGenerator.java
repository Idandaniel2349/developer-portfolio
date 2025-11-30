package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.config.DungeonConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Generates a single floor including all rooms, assigns
 * starting/exit rooms, and stores generated rooms in the shared room map.
 */
public class FloorGenerator {

    private final DungeonConfig dungeonConfig;
    private final Random random;
    private final RoomGenerator roomGenerator;

    public FloorGenerator(DungeonConfig dungeonConfig, Random random) throws IOException {
        this.dungeonConfig = dungeonConfig;
        this.random = random;
        roomGenerator = new RoomGenerator(dungeonConfig, random);
    }

    public Floor generateFloor(int floorNumber, Map<String, Room> roomMap){
        Floor floor = new Floor();
        floor.setFloorNumber(floorNumber);
        List<String> floorRoomsIds = new ArrayList<>();

        int maxRooms = dungeonConfig.getMaxRoomsPerFloor();
        int minRooms = dungeonConfig.getMinRoomsPerFloor();
        int numberOfRooms = random.nextInt(minRooms,maxRooms+1);

        int bossRoomNumber = random.nextInt(numberOfRooms);

        for(int roomNumber = 0; roomNumber < numberOfRooms; roomNumber++){
            Room room;
            if(roomNumber==bossRoomNumber){
                room = roomGenerator.generateRoom(floorNumber, roomNumber, true);
            }else{
                room = roomGenerator.generateRoom(floorNumber, roomNumber, false);
            }
            floorRoomsIds.add(room.getId()); // adds to floor
            roomMap.put(room.getId(), room); // adds to dungeon
        }
        floor.setNumberOfRooms(numberOfRooms);

        floor.setRoomIds(floorRoomsIds);

        // choose starting/exit rooms
        chooseFloorStartAndExit(floor, roomMap, numberOfRooms);

        // set exit direction for exit room
        String exitRoomId = floor.getExitRoomId();
        Room exitRoom = roomMap.get(exitRoomId);
        exitRoom.getExits().put("exit", null);

        return floor;
    }

    private void chooseFloorStartAndExit(Floor floor, Map<String, Room> roomMap, int numberOfRooms){
        int startRoomIndex = random.nextInt(numberOfRooms);
        int exitRoomIndex = random.nextInt(numberOfRooms);
        while(numberOfRooms!=1 && exitRoomIndex == startRoomIndex ){
            exitRoomIndex = random.nextInt(numberOfRooms);
        }

        String startingRoomId = floor.getRoomIds().get(startRoomIndex);
        String exitRoomId = floor.getRoomIds().get(exitRoomIndex);
        Room startingRoom = roomMap.get(startingRoomId);
        Room exitRoom = roomMap.get(exitRoomId);
        startingRoom.setStartingRoom(true);
        exitRoom.setExitRoom(true);
        floor.setStartingRoomId(startingRoomId);
        floor.setExitRoomId(exitRoomId);
    }
}
