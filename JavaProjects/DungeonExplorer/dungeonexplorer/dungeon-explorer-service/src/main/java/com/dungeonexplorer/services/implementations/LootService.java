package com.dungeonexplorer.services.implementations;

import com.dungeonexplorer.models.Enemy;
import com.dungeonexplorer.models.Loot;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.services.interfaces.ILootService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class LootService implements ILootService {
    private final Random random = new Random();

    @Override
    public List<Loot> generateLoot(Player player, List<Loot> possibleLoot) {
        List<Loot> lootToTake = new ArrayList<>();

        for(Loot loot : possibleLoot){
            if(random.nextDouble() < loot.getDropChance()){
                lootToTake.add(loot);
            }
        }

        return lootToTake;
    }
}
