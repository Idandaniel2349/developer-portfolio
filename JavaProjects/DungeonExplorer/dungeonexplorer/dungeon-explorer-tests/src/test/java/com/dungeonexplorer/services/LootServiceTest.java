package com.dungeonexplorer.services;

import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.implementations.LootService;
import com.dungeonexplorer.services.interfaces.ILootService;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LootServiceTest {

    private ILootService lootService;

    @Test
    public void handleLoot_single_success(){
        Player player = new Player("player");

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);

        Loot loot = new Loot(UUID.randomUUID(),item, 1, 1);

        List<Loot> loots = new ArrayList<>();
        loots.add(loot);

        lootService = new LootService();
        List<Loot> lootTaken = lootService.generateLoot(player,loots);

        Assert.assertEquals(1, lootTaken.size());
        Assert.assertEquals("Health Potion", lootTaken.get(0).getItem().getName());
        Assert.assertEquals(1, lootTaken.get(0).getQuantity());
    }
}
