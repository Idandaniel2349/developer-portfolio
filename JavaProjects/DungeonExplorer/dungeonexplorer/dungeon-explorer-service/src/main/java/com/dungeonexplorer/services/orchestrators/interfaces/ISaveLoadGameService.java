package com.dungeonexplorer.services.orchestrators.interfaces;

import com.dungeonexplorer.models.GameSession;

import java.io.IOException;
import java.util.UUID;

/**
 * Service responsible for save/load game to/from storage
 */
public interface ISaveLoadGameService {
    /**
     * Persists a game session to the save directory as a JSON file.
     *
     * @param gameSession The session to save.
     * @throws IOException If file creation or writing fails.
     */
    void save(GameSession gameSession) throws IOException;

    /**
     * Loads a saved game session by ID.
     *
     * @param gameSessionId The unique ID of the save file.
     * @return The loaded {@link GameSession}.
     * @throws IOException If the file is missing or cannot be parsed.
     */
    GameSession load(UUID gameSessionId) throws IOException;

    /**
     * Checks whether a saved game file exists for the given session ID.
     *
     * @param gameSessionId The ID to check.
     * @return True if a save file exists, false otherwise.
     */
    boolean exists(UUID gameSessionId);
}
