package com.dungeonexplorer.services.mappers.interfaces.templates;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.services.templates.EnemyTemplate;

public interface IMapEnemyTemplateToEnemy {
    Enemy map(EnemyTemplate enemyTemplate, int floorLevel);
}
