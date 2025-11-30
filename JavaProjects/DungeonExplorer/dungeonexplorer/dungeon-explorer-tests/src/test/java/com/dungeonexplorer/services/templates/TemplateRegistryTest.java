package com.dungeonexplorer.services.templates;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TemplateRegistryTest {

    @Test
    public void registerTest_success() throws IOException {
        TemplateRegistry templateRegistry = new TemplateRegistry();

        List<ItemTemplate> itemTemplateList = templateRegistry.getItemsList();
        Assert.assertEquals(3,itemTemplateList.size());

        List<EnemyTemplate> enemyTemplateList = templateRegistry.getEnemieslist();
        Assert.assertEquals(3,enemyTemplateList.size());

        List<EnemyTemplate> bossTemplateList = templateRegistry.getBosseslist();
        Assert.assertEquals(3,bossTemplateList.size());
    }

    @Test
    public void registerTest_failure() throws IOException {
        TemplateRegistry templateRegistry = new TemplateRegistry();

        EnemyTemplate enemyTemplate = templateRegistry.getEnemyById("snake");

        Assert.assertNull(enemyTemplate);
    }
}
