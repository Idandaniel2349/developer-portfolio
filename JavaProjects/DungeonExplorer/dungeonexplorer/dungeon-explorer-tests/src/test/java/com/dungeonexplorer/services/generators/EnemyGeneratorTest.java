package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.models.Item;
import com.dungeonexplorer.models.ItemType;
import com.dungeonexplorer.models.Loot;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.templates.TemplateRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class EnemyGeneratorTest {

    private Random random;
    private DungeonConfig dungeonConfig;
    private TemplateRegistry templateRegistry;
    private EnemyGenerator enemyGenerator;

    @BeforeEach
    public void setup() throws IOException {
        random = new Random(1234);
        dungeonConfig = new DungeonConfig();
        dungeonConfig.setEnemySpawnChance(1.0);
        dungeonConfig.setLootQuantity(2);
        dungeonConfig.setMinLootPerRoom(1);
        dungeonConfig.setMaxLootPerRoom(1);
        templateRegistry = new TemplateRegistry();
        enemyGenerator = new EnemyGenerator(random, dungeonConfig, templateRegistry);

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);
        Loot loot = new Loot(UUID.randomUUID(), item, 1, 2 );
        LootGenerator mockLootGenerator = Mockito.mock(LootGenerator.class);
        when(mockLootGenerator.generateLoot(1)).thenReturn(List.of(loot));

        // Inject the mock into the private field of enemyGenerator
        ReflectionTestUtils.setField(enemyGenerator, "lootGenerator", mockLootGenerator);
    }

    @Test
    public void generateSingleEnemy_success(){
        dungeonConfig.setEnemySpawnChance(1.0);

        List<Enemy> enemies = enemyGenerator.generateEnemies(1, 1, false);

        assertEquals(1, enemies.size());
        assertEquals("goblin", enemies.get(0).getTemplateId());
        assertEquals(2, enemies.get(0).getStats().getHealth());
        assertEquals(1, enemies.get(0).getStats().getAttack());
        assertEquals(2, enemies.get(0).getXp());
        assertEquals(1, enemies.get(0).getLoot().size());
        assertEquals("health_potion", enemies.get(0).getLoot().get(0).getItem().getTemplateId());
        assertFalse(enemies.get(0).isBoss());
    }

    @Test
    public void generateMultipleEnemy_partialSpawn_success(){
        dungeonConfig.setEnemySpawnChance(0.7);

        List<Enemy> enemies = enemyGenerator.generateEnemies(3, 2, false);

        assertEquals(2, enemies.size());

        assertEquals("goblin", enemies.get(0).getTemplateId());
        assertEquals(3, enemies.get(0).getStats().getHealth());
        assertEquals(2, enemies.get(0).getStats().getAttack());
        assertEquals(3, enemies.get(0).getXp());
        assertEquals(1, enemies.get(0).getLoot().size());
        assertEquals("health_potion", enemies.get(0).getLoot().get(0).getItem().getTemplateId());
        assertFalse(enemies.get(0).isBoss());

        assertEquals("skeleton", enemies.get(1).getTemplateId());
        assertEquals(3, enemies.get(1).getStats().getHealth());
        assertEquals(2, enemies.get(1).getStats().getAttack());
        assertEquals(3, enemies.get(1).getXp());
        assertEquals(1, enemies.get(1).getLoot().size());
        assertEquals("health_potion", enemies.get(1).getLoot().get(0).getItem().getTemplateId());
        assertFalse(enemies.get(1).isBoss());
    }

    @Test
    public void generateNoEnemy_success(){
        dungeonConfig.setEnemySpawnChance(0.0);

        List<Enemy> enemies = enemyGenerator.generateEnemies(1, 1, false);

        assertEquals(0, enemies.size());
    }

    @Test
    public void generateBoss_success(){
        dungeonConfig.setEnemySpawnChance(1.0);

        List<Enemy> enemies = enemyGenerator.generateEnemies(1, 1, true);

        assertEquals(1, enemies.size());
        assertEquals("lich_king", enemies.get(0).getTemplateId());
        assertEquals(4, enemies.get(0).getStats().getHealth());
        assertEquals(3, enemies.get(0).getStats().getAttack());
        assertEquals(4, enemies.get(0).getXp());
        assertEquals(1, enemies.get(0).getLoot().size());
        assertEquals("health_potion", enemies.get(0).getLoot().get(0).getItem().getTemplateId());
        assertTrue(enemies.get(0).isBoss());
    }
}
