package com.dungeonexplorer.services.interfaces;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.models.Player;

/**
 * Service responsible for handling combat between a player and an enemy.
 */
public interface ICombatService {
    /**
     * Executes a single round of combat between the player and enemy.
     * Mechanics:
     *     The player attacks first, reducing the enemy's health by the player's attack value.
     *     If the enemy survives, it attacks the player.
     *
     * @param player The attacking player.
     * @param enemy The defending enemy.
     * @return {true} if the enemy was defeated during this combat round; {false} otherwise.
     */
    void fight(Player player, Enemy enemy);
}
