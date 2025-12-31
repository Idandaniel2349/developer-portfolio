package com.dungeonexplorer.services.integration;

import com.dungeonexplorer.models.GameSession;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.orchestrators.implementations.NewGameOrchestrator;
import com.dungeonexplorer.services.orchestrators.implementations.SaveLoadGameService;
import com.dungeonexplorer.services.orchestrators.interfaces.IGameOrchestrator;
import com.dungeonexplorer.services.orchestrators.interfaces.ISaveLoadGameService;
import com.dungeonexplorer.services.persistence.GameSaveEntity;
import com.dungeonexplorer.services.persistence.GameSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

public class NewGameOrchestratorTest {

    @Mock
    private GameSessionRepository gameSessionRepository; // Mock the DB

    private ISaveLoadGameService saveLoadGameService;
    private IGameOrchestrator gameOrchestrator;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();

        // Manually inject the mock into the service
        saveLoadGameService = new SaveLoadGameService(gameSessionRepository);

        // Pass the service to the orchestrator
        gameOrchestrator = new NewGameOrchestrator(saveLoadGameService);
    }

    @Test
    public void startNewGameTest() throws IOException {

        // start new game
        GameSession gameSession = gameOrchestrator.startNewGame("Player1");
        UUID sessionId = gameSession.getSessionId();

        assertNotNull(gameSession.getSessionId());
        assertNotNull(gameSession.getPlayer());
        assertEquals("Player1", gameSession.getPlayer().getName());
        assertNotNull(gameSession.getDungeon());
        assertTrue(gameSession.isActive());

        Mockito.verify(gameSessionRepository, Mockito.times(1)).save(Mockito.any());

        // mock returned entity
        GameSaveEntity mockEntity = new GameSaveEntity();
        mockEntity.setSessionId(sessionId);
        mockEntity.setPlayerName(gameSession.getPlayer().getName());
        String jsonData = objectMapper.writeValueAsString(gameSession);
        mockEntity.setJsonData(jsonData);

        Mockito.when(gameSessionRepository.findById(sessionId))
                .thenReturn(Optional.of(mockEntity));

        // load saved game
        GameSession loadedGameSession = gameOrchestrator.loadSavedGame(gameSession.getSessionId());

        assertEquals(gameSession.getSessionId(), loadedGameSession.getSessionId());
        assertEquals(gameSession.getPlayer().getName(), loadedGameSession.getPlayer().getName());
        assertNotNull(loadedGameSession.getDungeon());
        assertTrue(loadedGameSession.isActive());
    }
}
