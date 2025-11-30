package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Item;
import com.dungeonexplorer.models.Loot;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.mappers.implementations.templates.MapItemTemplateListToItemList;
import com.dungeonexplorer.services.mappers.interfaces.templates.IMapItemTemplateListToItemList;
import com.dungeonexplorer.services.templates.TemplateRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Generates random loot items based on configuration and available item templates.
 */
public class LootGenerator {

    private final DungeonConfig dungeonConfig;
    private final Random random;
    private final List<Item> possibleItems;

    public  LootGenerator(DungeonConfig dungeonConfig, Random random, TemplateRegistry templateRegistry){
        this.dungeonConfig = dungeonConfig;
        this.random = random;

        IMapItemTemplateListToItemList mapItemTemplateListToItemList = new MapItemTemplateListToItemList();
        possibleItems = mapItemTemplateListToItemList.map(templateRegistry.getItemsList());
    }

    public List<Loot> generateLoot(int numLoot){
        List<Loot> lootList = new ArrayList<>();
        for(int i=0; i<numLoot; i++){
            Loot loot = new Loot();
            loot.setId(UUID.randomUUID());
            loot.setDropChance(dungeonConfig.getLootSpawnChance());
            loot.setQuantity(dungeonConfig.getLootQuantity());

            int randomIndex = random.nextInt(possibleItems.size());
            loot.setItem(possibleItems.get(randomIndex));

            lootList.add(loot);
        }
        return lootList;
    }
}
