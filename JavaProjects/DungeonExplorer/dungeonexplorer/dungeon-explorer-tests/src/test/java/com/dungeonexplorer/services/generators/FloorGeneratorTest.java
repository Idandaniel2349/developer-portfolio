package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.config.DungeonConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class FloorGeneratorTest {

    private Random random;
    private DungeonConfig dungeonConfig;

    private FloorGenerator floorGenerator;

    private RoomGenerator mockRoomGenerator;

    @BeforeEach
    public void setup() throws IOException {
        random = new Random();
        dungeonConfig = new DungeonConfig();

        floorGenerator = new FloorGenerator(dungeonConfig, random);

        mockRoomGenerator = Mockito.mock(RoomGenerator.class);
        Room room1 = new Room("floor1_room1", "Room1", "A Dungeon Room");
        Room room2 = new Room("floor1_room2", "Room2", "A Dungeon Room");
        Room room3 = new Room("floor1_room3", "Room3", "A Dungeon Room");
        Room room4 = new Room("floor1_room4", "Room4", "A Dungeon Room");
        Room room5 = new Room("floor1_room5", "Room5", "A Dungeon Room");
        when(mockRoomGenerator.generateRoom(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyBoolean())).thenReturn(room1,room2,room3,room4,room5);
        ReflectionTestUtils.setField(floorGenerator, "roomGenerator", mockRoomGenerator);
    }

    @Test
    public void generateFloor_singleRoom_success(){
        dungeonConfig.setMinRoomsPerFloor(1);
        dungeonConfig.setMaxRoomsPerFloor(1);

        Map<String, Room> roomMap = new HashMap<>();

        Floor floor = floorGenerator.generateFloor(1, roomMap);

        assertEquals(1, floor.getNumberOfRooms());
        assertEquals(1, roomMap.size());
        assertNotNull(floor.getStartingRoomId());
        assertNotNull(floor.getExitRoomId());
    }

    @Test
    public void generateFloor_MultipleRooms_success(){
        dungeonConfig.setMinRoomsPerFloor(5);
        dungeonConfig.setMaxRoomsPerFloor(5);

        Map<String, Room> roomMap = new HashMap<>();

        Floor floor = floorGenerator.generateFloor(1, roomMap);

        assertEquals(5, floor.getNumberOfRooms());
        assertEquals(5, roomMap.size());
        assertNotNull(floor.getStartingRoomId());
        assertNotNull(floor.getExitRoomId());
    }
}
