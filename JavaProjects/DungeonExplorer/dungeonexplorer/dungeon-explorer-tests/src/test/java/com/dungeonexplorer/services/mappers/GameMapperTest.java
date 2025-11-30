package com.dungeonexplorer.services.mappers;

import com.dungeonexplorer.dtos.GameSessionDTO;
import com.dungeonexplorer.mappers.GameMapper;
import com.dungeonexplorer.models.GameSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class GameMapperTest {

    GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    @Test
    public void gameSessionToDTO_test(){
        GameSession gameSession;
        GameSessionDTO expected;

        ObjectMapper mapper = new ObjectMapper();
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("GameSession.json")){
            gameSession = mapper.readValue(is, new TypeReference<GameSession>() {});
        }catch (IOException e){
            throw new RuntimeException("Failed to load items", e);
        }

//        try(InputStream is = getClass().getClassLoader().getResourceAsStream("GameSessionDTO.json")){
//            expected = mapper.readValue(is, new TypeReference<GameSessionDTO>() {});
//        }catch (IOException e){
//            throw new RuntimeException("Failed to load items", e);
//        }

        GameSessionDTO gameSessionDTO = gameMapper.toDTO(gameSession);
//        assertThat(gameSessionDTO)
//                .usingRecursiveComparison()
//                .isEqualTo(expected);
    }
}
