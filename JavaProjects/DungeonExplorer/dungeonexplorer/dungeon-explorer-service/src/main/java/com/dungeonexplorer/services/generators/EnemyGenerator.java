package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.mappers.implementations.templates.MapEnemyTemplateToEnemy;
import com.dungeonexplorer.services.mappers.interfaces.templates.IMapEnemyTemplateToEnemy;
import com.dungeonexplorer.services.templates.EnemyTemplate;
import com.dungeonexplorer.services.templates.TemplateRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates enemies for rooms based on templates, floor number,
 * spawn chance, and whether the room is a boss room.
 * <p>
 * Also attaches possible loot to each generated enemy.
 */
public class EnemyGenerator {

    private final Random random;
    private final DungeonConfig dungeonConfig;
    private final List<EnemyTemplate> possibleEnemies;
    private final List<EnemyTemplate> possibleBosses;
    private final IMapEnemyTemplateToEnemy mapEnemyTemplateListToEnemyList;

    private final LootGenerator lootGenerator;


    public EnemyGenerator(Random random, DungeonConfig dungeonConfig, TemplateRegistry templateRegistry){
        this.random = random;
        this.dungeonConfig = dungeonConfig;
        mapEnemyTemplateListToEnemyList = new MapEnemyTemplateToEnemy();
        possibleEnemies = templateRegistry.getEnemieslist();
        possibleBosses = templateRegistry.getBosseslist();
        lootGenerator = new LootGenerator(dungeonConfig, random, templateRegistry);
    }

    public List<Enemy> generateEnemies(int numEnemies, int floorNumber, boolean isBoss){
        List<Enemy> enemyList = new ArrayList<>();

        // for isBoss=true then numEnemies=1
        for(int i=0; i<numEnemies; i++){
            int enemyIndex;
            Enemy enemy;

            if(isBoss){
                enemyIndex = random.nextInt(possibleBosses.size());
                enemy = mapEnemyTemplateListToEnemyList.map(possibleBosses.get(enemyIndex), floorNumber+2);
                enemy.setBoss(true);
            }else{
                if(random.nextDouble() > dungeonConfig.getEnemySpawnChance()){
                    continue;
                }
                enemyIndex = random.nextInt(possibleEnemies.size());
                enemy = mapEnemyTemplateListToEnemyList.map(possibleEnemies.get(enemyIndex), floorNumber);
            }

            // handle enemy loot
            int minLoot = dungeonConfig.getMinLootPerRoom();
            int maxLoot = dungeonConfig.getMaxLootPerRoom();
            int numLoot = random.nextInt(minLoot, maxLoot+1);
            enemy.setLoot(lootGenerator.generateLoot(numLoot));

            enemyList.add(enemy);
        }

        return enemyList;
    }
}
