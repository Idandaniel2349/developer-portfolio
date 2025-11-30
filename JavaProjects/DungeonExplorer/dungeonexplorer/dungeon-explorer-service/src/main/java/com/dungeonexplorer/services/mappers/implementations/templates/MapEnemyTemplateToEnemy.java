package com.dungeonexplorer.services.mappers.implementations.templates;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.models.Stats;
import com.dungeonexplorer.services.mappers.interfaces.templates.IMapEnemyTemplateToEnemy;
import com.dungeonexplorer.services.templates.EnemyTemplate;

import java.util.UUID;

public class MapEnemyTemplateToEnemy implements IMapEnemyTemplateToEnemy {
    @Override
    public Enemy map(EnemyTemplate enemyTemplate, int floorNumber) {
        Enemy enemy = new Enemy();
        enemy.setId(UUID.randomUUID());
        enemy.setTemplateId(enemyTemplate.getId());
        enemy.setName(enemyTemplate.getName());
        enemy.setDefeated(enemyTemplate.isDefeated());

        //set stats
        Stats stats = new Stats();
        stats.setHealth(enemyTemplate.getBaseHealth() + floorNumber);
        stats.setAttack(enemyTemplate.getBaseAttack() + floorNumber - 1);
        stats.setDefense(0);
        enemy.setStats(stats);

        //set xp
        enemy.setXp(enemyTemplate.getBaseXp() + floorNumber);

        return enemy;
    }
}
