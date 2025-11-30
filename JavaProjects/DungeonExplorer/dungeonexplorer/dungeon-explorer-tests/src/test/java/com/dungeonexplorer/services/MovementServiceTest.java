package com.dungeonexplorer.services;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.implementations.MovementService;
import com.dungeonexplorer.services.interfaces.IMovementService;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovementServiceTest {

    private IMovementService movementService;

    @Test
    public void movePlayer_success(){
        Player player = new Player("player");
        Room northRoom = new Room("room_north","RoomNorth", "");
        Map<String, String> currentExits = new HashMap<>();
        currentExits.put("north", "room_north");
        Room current = new Room("room0","Room1", "");
        current.setExits(currentExits);
        player.setCurrentRoomId(current.getId());

        Dungeon dungeon = new Dungeon();
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("room0", current);
        roomMap.put("room_north", northRoom);
        dungeon.setRoomById(roomMap);

        movementService = new MovementService();

        boolean moved = movementService.movePlayer(player, "north",dungeon);

        Assert.assertTrue(moved);
        Assert.assertEquals(northRoom.getId(), player.getCurrentRoomId());
    }

    @Test
    public void movePlayer_failure(){
        Player player = new Player("player");
        Room northRoom = new Room("room_north","RoomNorth", "");
        Map<String, String> currentExits = new HashMap<>();
        currentExits.put("north", "room1");
        Room current = new Room("room0","Room1", "");
        current.setExits(currentExits);
        player.setCurrentRoomId(current.getId());

        Dungeon dungeon = new Dungeon();
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put("room0", current);
        roomMap.put("room_north", northRoom);
        dungeon.setRoomById(roomMap);

        movementService = new MovementService();

        boolean moved = movementService.movePlayer(player, "south", dungeon);

        Assert.assertFalse(moved);
        Assert.assertEquals(current.getId(), player.getCurrentRoomId());
    }
}
