package com.dungeonexplorer.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoomDTO {
    private String id;
    private String roomName;
    private String description;
    private Map<String,String> exits;
    private boolean isStartingRoom;
    private boolean isExitRoom;
    private boolean visited;
    private List<EnemyDTO> enemies;
    private boolean isBossRoom;
}
