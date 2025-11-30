package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.config.DungeonConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DungeonGeneratorTest {
    private DungeonConfig dungeonConfig;
    private DungeonGenerator dungeonGenerator;

    @BeforeEach
    public void setup() throws IOException {
        dungeonConfig = new DungeonConfig();
        dungeonConfig.setFloors(2);

        dungeonGenerator = new DungeonGenerator(dungeonConfig);

        Room room1 = new Room("floor1_room1", "Room1", "A Dungeon Room");
        Room room2 = new Room("floor1_room2", "Room2", "A Dungeon Room");
        List<String> floor1_rooms = new ArrayList<>();
        floor1_rooms.add(room1.getId());
        floor1_rooms.add(room2.getId());
        Floor floor1 = new Floor(1,2, room1.getId(), room2.getId(), floor1_rooms, false);

        Room room3 = new Room("floor2_room1", "Room1", "A Dungeon Room");
        Room room4 = new Room("floor2_room2", "Room2", "A Dungeon Room");
        List<String> floor2_rooms = new ArrayList<>();
        floor2_rooms.add(room3.getId());
        floor2_rooms.add(room4.getId());
        Floor floor2 = new Floor(1,2, room1.getId(), room2.getId(), floor2_rooms, false);

        FloorGenerator mockFloorGenerator = Mockito.mock(FloorGenerator.class);
        when(mockFloorGenerator.generateFloor(Mockito.anyInt(),Mockito.any())).thenReturn(floor1, floor2);
        ReflectionTestUtils.setField(dungeonGenerator, "floorGenerator", mockFloorGenerator);

        RoomConnector mockRoomConnector = Mockito.mock(RoomConnector.class);
        Mockito.doNothing().when(mockRoomConnector).connectRooms(Mockito.any(),Mockito.any());
        ReflectionTestUtils.setField(dungeonGenerator, "roomConnector", mockRoomConnector);
    }

    @Test
    public void generateDungeon_success(){
        Dungeon dungeon = dungeonGenerator.generateDungeon();

        assertEquals(2, dungeon.getFloors().size());
    }
}
