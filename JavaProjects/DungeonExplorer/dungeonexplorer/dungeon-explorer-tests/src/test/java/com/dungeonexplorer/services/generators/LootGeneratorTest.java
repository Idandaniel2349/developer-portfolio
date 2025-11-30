package com.dungeonexplorer.services.generators;

import com.dungeonexplorer.models.Loot;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.templates.TemplateRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;

public class LootGeneratorTest {

    private Random random;
    private DungeonConfig dungeonConfig;
    private TemplateRegistry templateRegistry;
    private LootGenerator lootGenerator;

    @BeforeEach
    public void setup() throws IOException {
        random = new Random(1234);
        dungeonConfig = new DungeonConfig();
        dungeonConfig.setLootSpawnChance(1.0);
        dungeonConfig.setLootQuantity(2);
        templateRegistry = new TemplateRegistry();
        lootGenerator = new LootGenerator(dungeonConfig, random, templateRegistry);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 8})
    public void generateLoot_variousCounts_success(int numLoot) {
        List<Loot> lootList = lootGenerator.generateLoot(numLoot);

        assertEquals(numLoot, lootList.size());
        for (Loot loot : lootList) {
            assertEquals(2, loot.getQuantity());
            assertEquals(1.0, loot.getDropChance());
        }
    }
}
