package com.dungeonexplorer.services.orchestrators.implementations;

import com.dungeonexplorer.models.Dungeon;
import com.dungeonexplorer.models.GameSession;
import com.dungeonexplorer.models.Player;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.generators.DungeonGenerator;
import com.dungeonexplorer.services.orchestrators.interfaces.IGameOrchestrator;
import com.dungeonexplorer.services.orchestrators.interfaces.ISaveLoadGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class NewGameOrchestrator implements IGameOrchestrator {

    private final DungeonConfig dungeonConfig;

    private final ISaveLoadGameService saveLoadGameService;

    public NewGameOrchestrator(ISaveLoadGameService saveLoadGameService) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        this.dungeonConfig = mapper.readValue(
                getClass().getClassLoader().getResourceAsStream("config/dungeon.yaml"),
                DungeonConfig.class
        );
        this.saveLoadGameService = saveLoadGameService;
    }

    @Override
    public GameSession startNewGame(String playerName) throws IOException {
        // Generate a dungeon
        DungeonGenerator dungeonGenerator = new DungeonGenerator(dungeonConfig);
        Dungeon dungeon = dungeonGenerator.generateDungeon();

        // Create a player
        if(playerName==null || playerName.isEmpty()){
            throw new BadRequestException("player name null or empty");
        }
        Player player = new Player( playerName);
        player.setCurrentFloorNumber(1);
        player.setCurrentRoomId(dungeon.getFloors().get(0).getStartingRoomId());

        // create GameSession
        GameSession gameSession = new GameSession(player, dungeon);

        saveLoadGameService.save(gameSession);

        return gameSession;
    }

    @Override
    public GameSession loadSavedGame(UUID gameSessionId) throws IOException {
        if(gameSessionId==null){
            throw new BadRequestException("gameSessionId is null");
        }

        return saveLoadGameService.load(gameSessionId);
    }

    @Override
    public void saveGame(GameSession gameSession) throws IOException {
        saveLoadGameService.save(gameSession);
    }
}
