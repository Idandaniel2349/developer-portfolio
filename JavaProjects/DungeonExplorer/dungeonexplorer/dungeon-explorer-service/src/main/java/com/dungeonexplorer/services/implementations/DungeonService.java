package com.dungeonexplorer.services.implementations;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;
import com.dungeonexplorer.services.interfaces.IDungeonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DungeonService implements IDungeonService {

    @Override
    public Room getStartingRoom(int floorNumber, Dungeon dungeon) {
        Floor floor = dungeon.getFloors().get(floorNumber);
        String startingRoomId = floor.getStartingRoomId();
        return dungeon.getRoomById().get(startingRoomId);
    }

    @Override
    public Floor getNextFloor(Floor currentFloor, Dungeon dungeon) {
        List<Floor> floors = dungeon.getFloors();
        int currentFloorIndex = floors.indexOf(currentFloor);
        if(currentFloorIndex >= 0 && currentFloorIndex < floors.size()-1){
            return floors.get(currentFloorIndex + 1);
        }

        // current floor is last
        return null;
    }

    @Override
    public Room getRoomById(String roomId, Dungeon dungeon) {
        return dungeon.getRoomById().get(roomId);
    }

    @Override
    public Floor getFloorByNumber(int floorNumber, Dungeon dungeon) {
        return dungeon.getFloors().get(floorNumber);
    }
}
