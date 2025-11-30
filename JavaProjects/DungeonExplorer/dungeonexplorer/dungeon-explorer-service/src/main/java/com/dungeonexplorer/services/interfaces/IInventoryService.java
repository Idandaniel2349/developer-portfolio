package com.dungeonexplorer.services.interfaces;

import com.dungeonexplorer.models.Item;
import com.dungeonexplorer.models.Player;

/**
 * Service responsible for managing a player's inventory.
 * Provides operations to add, remove, and use items.
 */
public interface IInventoryService {
    /**
     * Adds a specified quantity of an item to the player's inventory.
     *
     * @param player  The player whose inventory will be updated.
     * @param item    The item to add.
     * @param quantity The quantity of the item to add. Must be positive.
     */
    void addItem(Player player, Item item, int quantity);

    /**
     * Removes a specified quantity of an item from the player's inventory.
     * If the player does not have enough of the item, the method should
     * remove as many as available.
     *
     * @param player  The player whose inventory will be updated.
     * @param item    The item to remove.
     * @param quantity The quantity of the item to remove. Must be positive.
     */
    void removeItem(Player player, Item item, int quantity);

    /**
     * Uses a single instance of the specified item from the player's inventory.
     * The effect applied depends on the item's type:
     * The used item is removed from the inventory.
     *
     * @param player The player using the item.
     * @param templateId   The item template id to use.
     */
    void useItem(Player player, String templateId);
}
