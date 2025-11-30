package com.dungeonexplorer.services.interfaces;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Floor;
import com.dungeonexplorer.models.Room;

/**
 * Service responsible for specific dungeon operations
 */
public interface IDungeonService {
    /**
     * Retrieves the starting room of a given floor.
     *
     * <p>This retrieves the starting room ID defined in the floor metadata
     * and returns the corresponding {Room} instance from the dungeon.</p>
     *
     * @param floorNumber the floor number whose starting room is requested
     * @param dungeon the dungeon containing all floors and rooms
     * @return the starting room for the specified floor
     */
    Room getStartingRoom(int floorNumber, Dungeon dungeon);

    /**
     * Returns the next floor after the provided one.
     *
     * <p>If the current floor is the last floor in the dungeon,
     * the method returns {null}.</p>
     *
     * @param currentFloor the current floor the player is on
     * @param dungeon the dungeon containing all floors
     * @return the next floor, or {null} if the current floor is the final one
     */

    Floor getNextFloor(Floor currentFloor, Dungeon dungeon);

    /**
     * Retrieves a room by its unique ID from the dungeon.
     *
     * @param roomId the identifier of the desired room
     * @param dungeon the dungeon containing the mapped rooms
     * @return the room matching the given ID, or {null} if no such room exists
     */
    Room getRoomById(String roomId, Dungeon dungeon);

    /**
     * Retrieves a floor by its unique number from the dungeon.
     *
     * @param floorNumber the identifier of the desired floor
     * @param dungeon the dungeon containing the mapped rooms
     * @return the floor matching the given number, or {null} if no such room exists
     */
    Floor getFloorByNumber(int floorNumber, Dungeon dungeon);
}
