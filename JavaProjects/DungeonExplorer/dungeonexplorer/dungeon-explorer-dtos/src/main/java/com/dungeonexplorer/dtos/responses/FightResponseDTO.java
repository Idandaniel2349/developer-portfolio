package com.dungeonexplorer.dtos.responses;

import com.dungeonexplorer.dtos.EnemyDTO;
import com.dungeonexplorer.dtos.LootDTO;
import com.dungeonexplorer.dtos.PlayerDTO;
import com.dungeonexplorer.services.results.FightEnum;
import com.dungeonexplorer.services.results.FightResult;
import lombok.Data;

import java.util.List;
 @Data
public class FightResponseDTO {
     private FightResult result;
     private EnemyDTO enemy;
     private PlayerDTO player;
}
