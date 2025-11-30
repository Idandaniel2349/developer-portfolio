package com.dungeonexplorer.services.implementations;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.interfaces.IMovementService;
import org.springframework.stereotype.Service;

@Service
public class MovementService implements IMovementService {
    @Override
    public boolean movePlayer(Player player, String direction, Dungeon dungeon) {
        String currentRoomId = player.getCurrentRoomId();
        Room currentRoom = dungeon.getRoomById().get(currentRoomId);

        String nextRoomId = currentRoom.getExits().get(direction);
        if(nextRoomId == null){
            // current room doesn't have an exit in 'direction'
            return false;
        }

        Room nextRoom = dungeon.getRoomById().get(nextRoomId);
        if(nextRoom == null){
            return false;
        }

        player.setCurrentRoomId(nextRoomId);
        return true;
    }
}
