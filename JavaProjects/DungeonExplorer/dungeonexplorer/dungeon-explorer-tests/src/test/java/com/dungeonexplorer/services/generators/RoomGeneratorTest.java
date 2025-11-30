package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.templates.TemplateRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RoomGeneratorTest {

    private Random random;
    private DungeonConfig dungeonConfig;

    private RoomGenerator roomGenerator;

    @BeforeEach
    public void setup() throws IOException {
        random = new Random(1234);
        dungeonConfig = new DungeonConfig();
        dungeonConfig.setMinLootPerRoom(1);
        dungeonConfig.setMaxLootPerRoom(1);
        dungeonConfig.setMinEnemiesPerRoom(1);
        dungeonConfig.setMaxEnemiesPerRoom(1);

        roomGenerator = new RoomGenerator(dungeonConfig, random);

        Item item = new Item(UUID.randomUUID(), "health_potion","Health Potion", "Potion that heals", ItemType.HEALING, 1);
        Loot loot = new Loot(UUID.randomUUID(), item, 1, 2 );
        LootGenerator mockLootGenerator = Mockito.mock(LootGenerator.class);
        when(mockLootGenerator.generateLoot(1)).thenReturn(List.of(loot));
        ReflectionTestUtils.setField(roomGenerator, "lootGenerator", mockLootGenerator);

        Enemy enemy = new Enemy(UUID.randomUUID(),"goblin", " Goblin", new Stats(1,1,1), 1,  null, false, false);
        EnemyGenerator mockEnemyGenerator = Mockito.mock(EnemyGenerator.class);
        when(mockEnemyGenerator.generateEnemies(1,1, false)).thenReturn(List.of(enemy));
        ReflectionTestUtils.setField(roomGenerator, "enemyGenerator", mockEnemyGenerator);
    }

    @Test
    public void generateRoom_success(){
        Room room = roomGenerator.generateRoom(1,1, false);

        assertEquals("floor1_room1", room.getId());
        assertEquals("Room1", room.getRoomName());
        assertEquals("A dungeon Room", room.getDescription());
        assertEquals(1, room.getLoot().size());
        assertEquals(1, room.getEnemies().size());
        assertFalse(room.isBossRoom());
    }

    @Test
    public void generateBossRoom_success(){
        Enemy enemy = new Enemy(UUID.randomUUID(),"dragon", " Dragon", new Stats(3,2,0), 3,  null, false, true);
        EnemyGenerator mockEnemyGenerator = Mockito.mock(EnemyGenerator.class);
        when(mockEnemyGenerator.generateEnemies(1,1, true)).thenReturn(List.of(enemy));
        ReflectionTestUtils.setField(roomGenerator, "enemyGenerator", mockEnemyGenerator);

        Room room = roomGenerator.generateRoom(1,1, true);

        assertEquals("floor1_room1", room.getId());
        assertEquals("Room1", room.getRoomName());
        assertEquals("A dungeon Room", room.getDescription());
        assertEquals(1, room.getLoot().size());
        assertEquals(1, room.getEnemies().size());
        assertTrue(room.isBossRoom());
    }
}
