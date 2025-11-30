package com.dungeonexplorer.services;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.implementations.DungeonService;
import com.dungeonexplorer.services.interfaces.IDungeonService;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonServiceTest {

    private IDungeonService dungeonService;

    @Test
    public void getStartingRoom_success(){
        Floor floor1 = new Floor();
        floor1.setFloorNumber(0);

        Room startingRoom = new Room();
        startingRoom.setRoomName("Room1");
        startingRoom.setId("floor1_room1");
        floor1.setStartingRoomId(startingRoom.getId());

        Dungeon dungeon = new Dungeon();
        Map<String, Room> roomsById = new HashMap<>();
        roomsById.put(startingRoom.getId(), startingRoom);
        dungeon.setRoomById(roomsById);

        List<Floor> floors = new ArrayList<>();
        floors.add(floor1);
        dungeon.setFloors(floors);

        dungeonService = new DungeonService();

        Room roomReturned = dungeonService.getStartingRoom(floor1.getFloorNumber(), dungeon);

        Assert.assertEquals(startingRoom.getRoomName(), roomReturned.getRoomName());
    }

    @Test
    public void getNextFloor_success(){
        Dungeon dungeon = new Dungeon();

        Floor floor1 = new Floor();
        floor1.setFloorNumber(0);

        Floor floor2 = new Floor();
        floor2.setFloorNumber(1);

        List<Floor> floors = new ArrayList<>();
        floors.add(floor1);
        floors.add(floor2);

        dungeon.setFloors(floors);

        dungeonService = new DungeonService();

        Floor floor = dungeonService.getNextFloor(floor1, dungeon);

        Assert.assertEquals(1, floor.getFloorNumber());
    }

    @Test
    public void getNextFloor_Null(){
        Dungeon dungeon = new Dungeon();

        Floor floor1 = new Floor();
        floor1.setFloorNumber(0);

        Floor floor2 = new Floor();
        floor2.setFloorNumber(1);

        List<Floor> floors = new ArrayList<>();
        floors.add(floor1);
        floors.add(floor2);

        dungeon.setFloors(floors);

        dungeonService = new DungeonService();

        Floor floor = dungeonService.getNextFloor(floor2, dungeon);

        Assert.assertNull(floor);
    }

    @Test
    public void getRoomByName_success(){
        Dungeon dungeon = new Dungeon();

        Map<String, Room> roomMap = new HashMap<>();

        Room room1 = new Room();
        room1.setId("room1");
        room1.setRoomName("Room1");
        roomMap.put("room1", room1);

        Room room2 = new Room();
        room2.setId("room2");
        room2.setRoomName("Room2");
        roomMap.put("room2", room2);

        dungeon.setRoomById(roomMap);

        dungeonService = new DungeonService();

        Room room = dungeonService.getRoomById("room1", dungeon);

        Assert.assertEquals("Room1", room.getRoomName());
    }

    @Test
    public void getRoomByName_null(){
        Dungeon dungeon = new Dungeon();

        Map<String, Room> roomMap = new HashMap<>();

        Room room1 = new Room();
        room1.setId("room1");
        room1.setRoomName("Room1");
        roomMap.put("room1", room1);

        dungeon.setRoomById(roomMap);

        dungeonService = new DungeonService();

        Room room = dungeonService.getRoomById("room2", dungeon);

        Assert.assertNull(room);
    }
}
