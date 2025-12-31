package com.dungeonexplorer.services.orchestrators.implementations;

import com.dungeonexplorer.models.GameSession;
import com.dungeonexplorer.services.persistence.GameSaveEntity;
import com.dungeonexplorer.services.persistence.GameSessionRepository;
import com.dungeonexplorer.services.orchestrators.interfaces.ISaveLoadGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SaveLoadGameService implements ISaveLoadGameService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private GameSessionRepository gameSessionRepository;

    public SaveLoadGameService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    @Override
    public void save(GameSession gameSession) throws IOException {
        // Convert the Live Session to a String
        String jsonData = objectMapper.writeValueAsString(gameSession);

        // Wrap it in the Database Entity
        GameSaveEntity gameSaveEntity = new GameSaveEntity();
        gameSaveEntity.setSessionId(gameSession.getSessionId());
        gameSaveEntity.setSavedAt(LocalDateTime.now());
        gameSaveEntity.setJsonData(jsonData);

        // save to h2
        gameSessionRepository.save(gameSaveEntity);
    }

    @Override
    public GameSession load(UUID gameSessionId) throws IOException {
        GameSaveEntity gameSaveEntity = gameSessionRepository.findById(gameSessionId).orElseThrow();

        return objectMapper.readValue(gameSaveEntity.getJsonData(), GameSession.class);
    }
}
