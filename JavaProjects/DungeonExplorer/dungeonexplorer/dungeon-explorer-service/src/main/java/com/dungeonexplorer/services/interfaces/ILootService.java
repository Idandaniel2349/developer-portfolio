package com.dungeonexplorer.services.interfaces;

import com.dungeonexplorer.models.Loot;
import com.dungeonexplorer.models.Player;

import java.util.List;

/**
 * Service responsible for generating loot drops for the player.
 */
public interface ILootService {
    /**
     * Generates a list of loot items awarded to the player based on predefined drop chances.
     *
     * <p>The method iterates through all possible loot items and performs a probability
     * check for each one. If the generated random value is within the item's drop chance,
     * that item is granted to the player.</p>
     *
     * @param player the player for whom loot is being generated.
     * @param possibleLoot a list of all loot items that *may* drop in the current context
     * @return a list of loot items that successfully passed their drop chance check
     */

    List<Loot> generateLoot(Player player, List<Loot> possibleLoot);
}
