package com.dungeonexplorer.services;

import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.implementations.InventoryService;
import com.dungeonexplorer.services.interfaces.IInventoryService;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class InventoryServiceTest {

    private IInventoryService inventoryService;

    @Test
    public void addItemTest_success(){
        Player player = new Player("player");

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);

        inventoryService = new InventoryService();

        inventoryService.addItem(player, item, 2);

        Assert.assertTrue(player.getInventory().hasItem(item.getTemplateId()));
        Assert.assertEquals(2, player.getInventory().getItemQuantity(item));
    }

    @Test
    public void RemoveItemTest_success(){
        Player player = new Player("player");

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);

        inventoryService = new InventoryService();

        inventoryService.addItem(player, item, 2);

        Assert.assertTrue(player.getInventory().hasItem(item.getTemplateId()));
        Assert.assertEquals(2, player.getInventory().getItemQuantity(item));

        inventoryService.removeItem(player, item, 1);
        Assert.assertTrue(player.getInventory().hasItem(item.getTemplateId()));
        Assert.assertEquals(1, player.getInventory().getItemQuantity(item));

        inventoryService.removeItem(player, item, 3);
        Assert.assertFalse(player.getInventory().hasItem(item.getTemplateId()));
    }

    @Test
    public void useItemTest_success(){
        Player player = new Player("player");

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);

        inventoryService = new InventoryService();

        inventoryService.addItem(player, item, 2);

        Assert.assertTrue(player.getInventory().hasItem(item.getTemplateId()));
        Assert.assertEquals(2, player.getInventory().getItemQuantity(item));

        inventoryService.useItem(player, item.getTemplateId());

        Assert.assertEquals(1, player.getInventory().getItemQuantity(item));

        Assert.assertEquals(2, player.getStats().getHealth());
    }
}
