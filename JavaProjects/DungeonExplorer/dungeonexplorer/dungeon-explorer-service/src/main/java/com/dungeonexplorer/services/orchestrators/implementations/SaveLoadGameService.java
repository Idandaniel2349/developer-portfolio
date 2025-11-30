package com.dungeonexplorer.services.orchestrators.implementations;

import com.dungeonexplorer.models.GameSession;
import com.dungeonexplorer.services.orchestrators.interfaces.ISaveLoadGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class SaveLoadGameService implements ISaveLoadGameService {

    private final String saveFolder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SaveLoadGameService() throws IOException {
        this("/saves");
    }
    public SaveLoadGameService(String folderPath) throws IOException {
        this.saveFolder = folderPath;
        File folder = new File(saveFolder);
        if(!folder.exists()){
            boolean created = folder.mkdir();
            if(!created){
                throw new IOException("Failed to create folder " + saveFolder);
            }
        }
    }

    @Override
    public void save(GameSession gameSession) throws IOException {
        File saveFile = new File(saveFolder + gameSession.getSessionId() + ".json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, gameSession);
    }

    @Override
    public GameSession load(UUID gameSessionId) throws IOException {
        File saveFile = new File(saveFolder + gameSessionId + ".json");
        if(!saveFile.exists()){
            throw new IOException("save file " + gameSessionId +" not found");
        }
        return objectMapper.readValue(saveFile, GameSession.class);
    }

    @Override
    public boolean exists(UUID gameSessionId) {
        File saveFile = new File(saveFolder + gameSessionId + ".json");
        return saveFile.exists();
    }
}
