package com.dungeonexplorer.services.interfaces;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.models.Item;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.services.results.FightEnum;
import com.dungeonexplorer.services.results.FightResult;
import com.dungeonexplorer.services.results.MoveEnum;
import com.dungeonexplorer.services.results.MoveResult;

/**
 * Service responsible for the main game functions.
 */
public interface IGameEngineService {
    /**
     * Handles the full player movement workflow, including:
     * <ul>
     *     <li>Validating the requested direction</li>
     *     <li>Performing movement between rooms</li>
     *     <li>Handling exit-room logic and floor transitions</li>
     *     <li>Triggering room-entry events such as loot generation</li>
     * </ul>
     *
     * @param player   The player attempting to move.
     * @param direction The direction chosen by the player (e.g., "north", "south", "exit").
     * @param dungeon   The current dungeon containing all floors and rooms.
     * @return A MoveResult enum describing the outcome.
     */
    MoveResult movePlayer(Player player, String direction, Dungeon dungeon);

    /**
     * Performs a combat round between the player and an enemy.
     * Handles:
     * <ul>
     *     <li>Applying damage to both sides</li>
     *     <li>Checking for victory or defeat for both player and enemy</li>
     *     <li>Granting loot and XP on enemy defeat</li>
     *     <li>Marking bosses as defeated to unlock floor exit</li>
     * </ul>
     *
     * @param player The player participating in combat.
     * @param enemy  The enemy being fought.
     * @param dungeon The dungeon, used to update room or floor state.
     * @return A FightResult describing the outcome.
     */
    FightResult fightEnemy(Player player, Enemy enemy, Dungeon dungeon);

    /**
     * Uses a consumable item from the player’s inventory.
     * Applies the item’s effect and updates inventory state.
     *
     * @param player     The player using the item.
     * @param templateId The ID of the item template to use.
     */
    void useItem(Player player, String templateId);
}
