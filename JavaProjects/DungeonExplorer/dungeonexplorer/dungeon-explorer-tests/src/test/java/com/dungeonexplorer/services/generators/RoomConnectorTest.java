package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RoomConnectorTest {
    private Random mockRandom;
    private RoomConnector connector;

    private Room startRoom;
    private Room exitRoom;
    private Room middleRoom1;
    private Room middleRoom2;
    private Floor floor;
    private Dungeon dungeon;

    @BeforeEach
    public void setup() {
        mockRandom = Mockito.mock(Random.class);
        connector = new RoomConnector(mockRandom);

        // Create rooms
        startRoom = new Room("start", "Start", "Starting Room");
        exitRoom = new Room("exit", "Exit", "Exit Room");
        middleRoom1 = new Room("m1", "Middle1", "Middle Room 1");
        middleRoom2 = new Room("m2", "Middle2", "Middle Room 2");

        // get all room ids
        String startRoomId = startRoom.getId();
        String exitRoomId = exitRoom.getId();
        String middleRoom1Id = middleRoom1.getId();
        String middleRoom2Id = middleRoom2.getId();

        // Prepare floor
        floor = new Floor();
        floor.setStartingRoomId(startRoom.getId());
        floor.setExitRoomId(exitRoom.getId());
        floor.setRoomIds(List.of(startRoomId, middleRoom1Id, middleRoom2Id, exitRoomId));

        // set dungeon
        dungeon = new Dungeon();
        List<Floor> floors = new ArrayList<>();
        dungeon.setFloors(floors);

        Map<String, Room> roomsById = new HashMap<>();
        roomsById.put(startRoomId,startRoom);
        roomsById.put(middleRoom1Id,middleRoom1);
        roomsById.put(middleRoom2Id,middleRoom2);
        roomsById.put(exitRoomId,exitRoom);
        dungeon.setRoomById(roomsById);

        // Make random deterministic (cycle directions: east, north, south, west...)
        when(mockRandom.nextInt(4)).thenReturn(1, 0, 2, 3, 1, 0, 2, 3, 0, 1);
    }

    @Test
    public void testConnectRooms_coordinatesAndExits() {
        connector.connectRooms(floor, dungeon.getRoomById());

        // Starting room is at 0,0
        assertEquals(0, startRoom.getX());
        assertEquals(0, startRoom.getY());

        // All rooms have unique coordinates
        Set<String> coordinates = new HashSet<>();
        for (String roomId : floor.getRoomIds()) {
            Room room = dungeon.getRoomById().get(roomId);
            String key = room.getX() + "," + room.getY();
            assertFalse(coordinates.contains(key), "Duplicate coordinates for room: " + room.getId());
            coordinates.add(key);
        }

        // Verify that exits are bidirectional
        for (String roomId : floor.getRoomIds()) {
            Room room = dungeon.getRoomById().get(roomId);
            room.getExits().forEach((dir, targetId) -> {
                String target = floor.getRoomIds().stream()
                        .filter(r -> r.equals(targetId))
                        .findFirst()
                        .orElse(null);
                assertNotNull(target);

                // Confirm the reverse exit exists
                Room targetRoom = dungeon.getRoomById().get(targetId);
                boolean reverseExists = targetRoom.getExits().containsValue(room.getId());
                assertTrue(reverseExists, "Exit not bidirectional for room " + room.getId());
            });
        }
    }
}
