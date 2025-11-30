package com.dungeonexplorer.services.orchestrators.interfaces;

import com.dungeonexplorer.models.GameSession;
import com.dungeonexplorer.services.generators.DungeonGenerator;

import java.io.IOException;
import java.util.UUID;

/**
 * Orchestrator responsible for game
 **/

public interface IGameOrchestrator {
    /**
     * Starts a brand new game session.
     * Responsibilities:
     * <ul>
     *     <li>Loads dungeon configuration</li>
     *     <li>Generates a complete dungeon using {@link DungeonGenerator}</li>
     *     <li>Creates a new player at the starting room of floor 1</li>
     *     <li>Persists the new game session</li>
     * </ul>
     *
     * @param playerName The name of the new player.
     * @return A fully initialized {@link GameSession}.
     * @throws IOException If dungeon configuration or save file cannot be loaded/saved.
     */
    GameSession startNewGame(String playerName) throws IOException;

    /**
     * Loads an existing saved game session from storage.
     *
     * @param gameSessionId The unique ID of the saved game session.
     * @return The restored {@link GameSession}.
     * @throws IOException If the save file cannot be found or parsed.
     */
    GameSession loadSavedGame(UUID gameSessionId) throws IOException;

    /**
     * Saves the current game session to persistent storage.
     *
     * @param gameSession The session to save.
     * @throws IOException If writing to disk fails.
     */
    void saveGame(GameSession gameSession) throws IOException;
}
