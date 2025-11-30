package com.dungeonexplorer;

import com.dungeonexplorer.dtos.GameSessionDTO;
import com.dungeonexplorer.dtos.RoomDTO;
import com.dungeonexplorer.dtos.requests.*;
import com.dungeonexplorer.dtos.responses.FightResponseDTO;
import com.dungeonexplorer.dtos.responses.MoveResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@SpringBootTest(classes = DungeonExplorerApplication.class)
@AutoConfigureMockMvc
public class GameControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID sessionId;

    @Test
    public void createNewGame_success() throws Exception {
        GameSessionDTO gameSessionDTO = createNewGame();

        // Assertions
        assertNotNull(gameSessionDTO.getSessionId());
        assertNotNull(gameSessionDTO.getPlayer());
        assertNotNull(gameSessionDTO.getDungeon());
        assertEquals("Player1", gameSessionDTO.getPlayer().getName());
    }

    @Test
    public void loadGame_success() throws Exception {
        GameSessionDTO gameSessionDTO = createNewGame();

        LoadGameRequestDTO loadGameRequestDTO = new LoadGameRequestDTO();
        loadGameRequestDTO.setSessionId(gameSessionDTO.getSessionId());

        // convert request dto to json
        String jsonRequestLoad = objectMapper.writeValueAsString(loadGameRequestDTO);

        // Call the new game post request
        MvcResult mvcResultLoad = mockMvc.perform(post("/game/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestLoad))
                .andExpect(status().isOk())
                .andReturn();

        // get response json
        String responseJsonLoad = mvcResultLoad.getResponse().getContentAsString();

        // map to dto
        GameSessionDTO gameSessionDTOLoad = objectMapper.readValue(responseJsonLoad, GameSessionDTO.class);

        // Assertions
        assertNotNull(gameSessionDTOLoad.getSessionId());
        assertNotNull(gameSessionDTOLoad.getPlayer());
        assertNotNull(gameSessionDTOLoad.getDungeon());
        assertEquals("Player1", gameSessionDTOLoad.getPlayer().getName());
    }

    @Test
    public void move_success() throws Exception {
        GameSessionDTO gameSessionDTO = createNewGame();

        // get a viable direction
        String currentRoomId = gameSessionDTO.getPlayer().getCurrentRoomId();
        RoomDTO currentRoom = gameSessionDTO.getDungeon().getRoomById().get(currentRoomId);

        String direction = currentRoom.getExits().keySet().iterator().next();

        // set up move request and call it
        MoveRequestDTO moveRequestDTO = new MoveRequestDTO();
        moveRequestDTO.setSessionId(gameSessionDTO.getSessionId());
        moveRequestDTO.setDirection(direction);

        String jsonRequest = objectMapper.writeValueAsString(moveRequestDTO);

        MvcResult mvcResult = mockMvc.perform(post("/game/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        MoveResponseDTO moveResponseDTO = objectMapper.readValue(responseJson, MoveResponseDTO.class);

        // Assertions
        assertNotNull(moveResponseDTO.getPlayer());
        assertNotNull(moveResponseDTO.getMoveResult());
        assertNotNull(moveResponseDTO.getMoveResult().getMoveEnum());
        assertNotEquals(currentRoomId, moveResponseDTO.getPlayer().getCurrentRoomId());
    }

    @Test
    public void fight_success() throws Exception {
        GameSessionDTO gameSessionDTO = createNewGame();

        // get an enemy
        String currentRoomId = gameSessionDTO.getPlayer().getCurrentRoomId();
        RoomDTO currentRoom = gameSessionDTO.getDungeon().getRoomById().get(currentRoomId);

        if(currentRoom.getEnemies().isEmpty()){
            System.out.println("no enemies in Room");
            return;
        }

        UUID enemyId = currentRoom.getEnemies().get(0).getId();

        // set up fight request and call it
        FightRequestDTO fightRequestDTO = new FightRequestDTO();
        fightRequestDTO.setSessionId(gameSessionDTO.getSessionId());
        fightRequestDTO.setEnemyId(enemyId);

        String jsonRequest = objectMapper.writeValueAsString(fightRequestDTO);

        MvcResult mvcResult = mockMvc.perform(post("/game/fight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        FightResponseDTO fightResponseDTO = objectMapper.readValue(responseJson, FightResponseDTO.class);

        // Assertions
        assertNotNull(fightResponseDTO.getEnemy());
        assertNotNull(fightResponseDTO.getPlayer());
        assertNotNull(fightResponseDTO.getResult());
        assertNotNull(fightResponseDTO.getResult().getFightEnum());
        assertEquals(enemyId, fightResponseDTO.getEnemy().getId());
    }

    private GameSessionDTO createNewGame() throws Exception {
        NewGameRequestDTO newGameRequestDTO = new NewGameRequestDTO();
        newGameRequestDTO.setPlayerName("Player1");

        // convert request dto to json
        String jsonRequest = objectMapper.writeValueAsString(newGameRequestDTO);

        // Call the new game post request
        MvcResult mvcResult = mockMvc.perform(post("/game/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // get response json
        String responseJson = mvcResult.getResponse().getContentAsString();

        // map to dto
        return objectMapper.readValue(responseJson, GameSessionDTO.class);
    }

}
