package com.dungeonexplorer.services.implementations;

import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.interfaces.*;
import com.dungeonexplorer.services.results.FightEnum;
import com.dungeonexplorer.services.results.FightResult;
import com.dungeonexplorer.services.results.MoveEnum;
import com.dungeonexplorer.services.results.MoveResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameEngineService implements IGameEngineService {

    private final IMovementService movementService;

    private final ICombatService combatService;

    private final ILootService lootService;

    private final IInventoryService inventoryService;

    private final IDungeonService dungeonService;


    public GameEngineService(IMovementService movementService, ICombatService combatService, ILootService lootService, IInventoryService inventoryService, IDungeonService dungeonService) {
        this.movementService = movementService;
        this.combatService = combatService;
        this.lootService = lootService;
        this.inventoryService = inventoryService;
        this.dungeonService = dungeonService;
    }

    @Override
    public MoveResult movePlayer(Player player, String direction, Dungeon dungeon) {
        MoveResult moveResult = new MoveResult();
        boolean isFloorTransition = false;

        int currentFloorNumber = player.getCurrentFloorNumber();
        Floor floor = dungeonService.getFloorByNumber(player.getCurrentFloorNumber()-1, dungeon);
        String currentRoomId = player.getCurrentRoomId();
        Room currentRoom = dungeonService.getRoomById(currentRoomId, dungeon);

        // handle exit room case
        if(currentRoom.isExitRoom() && direction.equals("exit")){
            // if floor boss defeated do floor transition
            if(floor.isBossDefeated()){
                // check if final floor
                if(currentFloorNumber == dungeon.getFloors().size()){
                    // handle winning
                    moveResult.setMoveEnum(MoveEnum.GAME_WON);
                    return moveResult;
                }
                // floor transition
                player.setCurrentFloorNumber(currentFloorNumber+1);
                player.setCurrentRoomId(dungeonService.getStartingRoom(currentFloorNumber,dungeon).getId());
                isFloorTransition = true;
            }else{
                moveResult.setMoveEnum(MoveEnum.FLOOR_BOSS_NOT_DEFEATED);
                return moveResult;
            }
        }else{
            boolean moved = movementService.movePlayer(player,direction, dungeon);

            if(!moved){
                // Invalid move
                moveResult.setMoveEnum(MoveEnum.INVALID_MOVE);
                return moveResult;
            }
        }

        // do room entry logic
        Room NewCurrentRoom = dungeonService.getRoomById(player.getCurrentRoomId(), dungeon);
        if(!NewCurrentRoom.isVisited()){
            NewCurrentRoom.setVisited(true);
            List<Loot> lootTaken = lootService.generateLoot(player, NewCurrentRoom.getLoot());
            handleLoot(player, lootTaken);
            moveResult.setLootReceived(lootTaken);
        }
        moveResult.setMoveEnum(isFloorTransition? MoveEnum.FLOOR_TRANSITION : MoveEnum.MOVED);
        return moveResult;
    }

    @Override
    public FightResult fightEnemy(Player player, Enemy enemy, Dungeon dungeon) {
        combatService.fight(player, enemy);
        FightResult fightResult = new FightResult();

        if(player.getStats().getHealth()<=0){
            // handle game over
            fightResult.setFightEnum(FightEnum.PLAYER_DEFEATED);
            return fightResult;
        }

        if(!enemy.isDefeated()){
            fightResult.setFightEnum(FightEnum.ONGOING);
            return fightResult;
        }

        // enemy defeated
        List<Loot> lootTaken = lootService.generateLoot(player, enemy.getLoot());
        handleLoot(player, lootTaken);
        player.addXp(enemy.getXp());

        // get room and remove enemies
        String currentRoomId = player.getCurrentRoomId();
        Room currentRoom = dungeonService.getRoomById(currentRoomId, dungeon);
        currentRoom.getEnemies().remove(enemy);

        // if boss defeated set floor boss defeated to true
        if(enemy.isBoss()){
            Floor floor = dungeonService.getFloorByNumber(player.getCurrentFloorNumber()-1, dungeon);
            floor.setBossDefeated(true);
        }

        fightResult.setFightEnum(FightEnum.ENEMY_DEFEATED);
        fightResult.setLootReceived(lootTaken);
        return fightResult;
    }

    @Override
    public void useItem(Player player, String templateId) {
        inventoryService.useItem(player,templateId);
    }

    private void handleLoot(Player player, List<Loot> lootTaken){
        for(Loot loot: lootTaken){
            inventoryService.addItem(player, loot.getItem(), loot.getQuantity());
        }
    }
}
