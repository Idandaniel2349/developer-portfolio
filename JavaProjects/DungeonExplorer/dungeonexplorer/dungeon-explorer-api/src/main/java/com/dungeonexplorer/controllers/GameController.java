package com.dungeonexplorer.controllers;


import com.dungeonexplorer.dtos.GameSessionDTO;
import com.dungeonexplorer.dtos.requests.*;
import com.dungeonexplorer.dtos.responses.FightResponseDTO;
import com.dungeonexplorer.dtos.responses.MoveResponseDTO;
import com.dungeonexplorer.dtos.responses.UseItemResponseDTO;
import com.dungeonexplorer.mappers.GameMapper;
import com.dungeonexplorer.models.*;
import com.dungeonexplorer.services.interfaces.IGameEngineService;
import com.dungeonexplorer.services.orchestrators.interfaces.IGameOrchestrator;
import com.dungeonexplorer.services.results.FightEnum;
import com.dungeonexplorer.services.results.FightResult;
import com.dungeonexplorer.services.results.MoveEnum;
import com.dungeonexplorer.services.results.MoveResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {
    private final IGameOrchestrator gameOrchestrator;
    private final IGameEngineService gameEngineService;

    public GameController(IGameOrchestrator gameOrchestrator, IGameEngineService gameEngineService){
        this.gameOrchestrator = gameOrchestrator;
        this.gameEngineService = gameEngineService;
    }

    /**
     * Starts a new game session for a player.
     *
     * @param newGameRequestDTO contains the player's name
     * @return DTO representing the new game session
     */
    @PostMapping("/new")
    public GameSessionDTO createNewGame(@RequestBody NewGameRequestDTO newGameRequestDTO) throws IOException {
        String playerName = newGameRequestDTO.getPlayerName();
        GameSession gameSession = gameOrchestrator.startNewGame(playerName);
        return GameMapper.instance.toDTO(gameSession);
    }

    /**
     * Loads an existing saved game session.
     *
     * @param loadGameRequestDTO contains the session ID
     * @return DTO representing the loaded game session
     */
    @PostMapping("/load")
    public GameSessionDTO loadGame(@RequestBody LoadGameRequestDTO loadGameRequestDTO) throws IOException {
        GameSession gameSession = gameOrchestrator.loadSavedGame(loadGameRequestDTO.getSessionId());
        return GameMapper.instance.toDTO(gameSession);
    }

    /**
     * Moves the player in the specified direction within the dungeon.
     *
     * @param moveRequestDTO contains session ID and direction
     * @return DTO with updated player and room state after the move
     */
    @PostMapping("/move")
    public MoveResponseDTO move(@RequestBody MoveRequestDTO moveRequestDTO) throws IOException {
        GameSession gameSession = gameOrchestrator.loadSavedGame(moveRequestDTO.getSessionId());
        if(!gameSession.isActive()){
            return new MoveResponseDTO();
        }
        Player player = gameSession.getPlayer();
        Dungeon dungeon = gameSession.getDungeon();
        MoveResult moveResult = gameEngineService.movePlayer(player,moveRequestDTO.getDirection(),dungeon);

        // update data
        gameSession.setPlayer(player);
        gameSession.setDungeon(dungeon);
        if(moveResult.getMoveEnum().equals(MoveEnum.GAME_WON)){
            gameSession.setActive(false);
        }
        gameOrchestrator.saveGame(gameSession);

        // build and return response
        MoveResponseDTO moveResponseDTO = new MoveResponseDTO();
        moveResponseDTO.setMoveResult(moveResult);
        moveResponseDTO.setPlayer(GameMapper.instance.toDTO(player));
        Room currentRoom = dungeon.getRoomById().get(player.getCurrentRoomId());
        moveResponseDTO.setCurrentRoom(GameMapper.instance.toDTO(currentRoom));
        return moveResponseDTO;
    }

    /**
     * Executes a fight between the player and a specific enemy.
     *
     * @param fightRequestDTO contains session ID and enemy ID
     * @return DTO with fight results and updated player and enemy states
     */
    @PostMapping("/fight")
    public FightResponseDTO fight(@RequestBody FightRequestDTO fightRequestDTO) throws IOException {
        GameSession gameSession = gameOrchestrator.loadSavedGame(fightRequestDTO.getSessionId());
        if(!gameSession.isActive()){
            return new FightResponseDTO();
        }

        UUID enemyId = fightRequestDTO.getEnemyId();
        Player player = gameSession.getPlayer();
        Dungeon dungeon = gameSession.getDungeon();

        String currentRoomId = player.getCurrentRoomId();
        Room currentRoom = dungeon.getRoomById().get(currentRoomId);

        // get enemy from room by id
        List<Enemy> roomEnemies = currentRoom.getEnemies();
        Enemy enemy = roomEnemies
                .stream()
                .filter(e -> e.getId().equals(enemyId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Enemy not found"));


        FightResult fightResult = gameEngineService.fightEnemy(gameSession.getPlayer(),enemy, dungeon);

        // update data
        gameSession.setPlayer(player);
        gameSession.setDungeon(dungeon);
        if(fightResult.getFightEnum().equals(FightEnum.PLAYER_DEFEATED)){
            gameSession.setActive(false);
        }
        gameOrchestrator.saveGame(gameSession);

        // build and return response
        FightResponseDTO fightResponseDTO = new FightResponseDTO();
        fightResponseDTO.setResult(fightResult);
        fightResponseDTO.setEnemy(GameMapper.instance.toDTO(enemy));
        fightResponseDTO.setPlayer(GameMapper.instance.toDTO(player));
        return fightResponseDTO;
    }

    /**
     * Uses an item from the player's inventory.
     *
     * @param useItemRequestDTO contains session ID and item template ID
     * @return DTO with updated player state
     */
    @PostMapping("/use-item")
    public UseItemResponseDTO useItem(@RequestBody UseItemRequestDTO useItemRequestDTO) throws IOException {
        GameSession gameSession = gameOrchestrator.loadSavedGame(useItemRequestDTO.getSessionId());
        if(!gameSession.isActive()){
            return new UseItemResponseDTO();
        }

        gameEngineService.useItem(gameSession.getPlayer(), useItemRequestDTO.getItemTemplateId());

        // update data
        gameOrchestrator.saveGame(gameSession);

        // build and return response
        UseItemResponseDTO useItemResponseDTO = new UseItemResponseDTO();
        useItemResponseDTO.setPlayer(GameMapper.instance.toDTO(gameSession.getPlayer()));
        return useItemResponseDTO;
    }
}
