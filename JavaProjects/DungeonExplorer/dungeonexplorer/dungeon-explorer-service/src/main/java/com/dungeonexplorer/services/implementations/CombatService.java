package com.dungeonexplorer.services.implementations;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.services.interfaces.ICombatService;
import org.springframework.stereotype.Service;

@Service
public class CombatService implements ICombatService {
    @Override
    public void fight(Player player, Enemy enemy) {
        // player attacks enemy
        enemy.getStats().decrementToHealth(player.getStats().getAttack());

        // if enemy still alive enemy attacks
        if(enemy.getStats().getHealth()>0){
            player.getStats().decrementToHealth(enemy.getStats().getAttack());
        }else{
            enemy.setDefeated(true);
        }
    }
}
