package com.dungeonexplorer.services.interfaces;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Player;
/**
 * Service responsible for moving a player between rooms within a dungeon floor.
 * Handles movement validation and updates the player's current room.
 */
public interface IMovementService {

    /**
     * Attempts to move the player in the specified direction from their current room.
     * The move succeeds only if the current room has an exit in the given direction.
     *
     * @param player    The player to move.
     * @param direction The direction in which the player wants to move (e.g., "north", "east").
     * @return {true} if the move was successful; {false} if the move is invalid.
     */
    boolean movePlayer(Player player, String direction, Dungeon dungeon);
}
