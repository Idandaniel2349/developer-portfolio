package com.dungeonexplorer.services.integration;

import com.dungeonexplorer.models.GameSession;
import com.dungeonexplorer.services.config.DungeonConfig;
import com.dungeonexplorer.services.orchestrators.implementations.NewGameOrchestrator;
import com.dungeonexplorer.services.orchestrators.implementations.SaveLoadGameService;
import com.dungeonexplorer.services.orchestrators.interfaces.IGameOrchestrator;
import com.dungeonexplorer.services.orchestrators.interfaces.ISaveLoadGameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

public class NewGameOrchestratorTest {
    @TempDir
    Path tempDir;

    @Test
    public void startNewGameTest() throws IOException {
        ISaveLoadGameService saveLoadGameService = new SaveLoadGameService(tempDir.toFile().getAbsolutePath());
        IGameOrchestrator gameOrchestrator = new NewGameOrchestrator(saveLoadGameService);

        // start new game
        GameSession gameSession = gameOrchestrator.startNewGame("Player1");

        assertNotNull(gameSession.getSessionId());
        assertNotNull(gameSession.getPlayer());
        assertEquals("Player1", gameSession.getPlayer().getName());
        assertNotNull(gameSession.getDungeon());
        assertTrue(gameSession.isActive());

        // load saved game
        GameSession loadedGameSession = gameOrchestrator.loadSavedGame(gameSession.getSessionId());

        assertEquals(gameSession.getSessionId(), loadedGameSession.getSessionId());
        assertEquals(gameSession.getPlayer().getName(), loadedGameSession.getPlayer().getName());
        assertNotNull(loadedGameSession.getDungeon());
        assertTrue(loadedGameSession.isActive());
    }
}
