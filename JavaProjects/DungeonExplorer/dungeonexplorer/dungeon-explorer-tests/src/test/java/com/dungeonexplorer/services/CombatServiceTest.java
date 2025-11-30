package com.dungeonexplorer.services;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.models.Stats;
import com.dungeonexplorer.services.implementations.CombatService;
import com.dungeonexplorer.services.interfaces.ICombatService;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CombatServiceTest {

    private ICombatService combatService;

    @Test
    public void fightTest_EnemyLives(){
        Player player = new Player( "player");
        player.getStats().addToHealth(5);
        player.getStats().addToAttack(1);
        Enemy enemy = new Enemy(UUID.randomUUID(),"goblin","Goblin", new Stats(), 2, null, false, false);
        enemy.getStats().addToHealth(3);
        enemy.getStats().addToAttack(1);

        combatService = new CombatService();
        combatService.fight(player,enemy);

        Assert.assertFalse(enemy.isDefeated());
        Assert.assertEquals(3, enemy.getStats().getHealth());
        Assert.assertEquals(5, player.getStats().getHealth());
    }

    @Test
    public void fightTest_EnemyDies(){
        Player player = new Player( "player");
        player.getStats().addToAttack(1);
        Enemy enemy = new Enemy(UUID.randomUUID(),"goblin","Goblin", new Stats(), 2, null,false, false);

        combatService = new CombatService();
        combatService.fight(player,enemy);

        Assert.assertTrue(enemy.isDefeated());
        Assert.assertEquals(0, enemy.getStats().getHealth());
        Assert.assertEquals(1, player.getStats().getHealth());
    }
}
