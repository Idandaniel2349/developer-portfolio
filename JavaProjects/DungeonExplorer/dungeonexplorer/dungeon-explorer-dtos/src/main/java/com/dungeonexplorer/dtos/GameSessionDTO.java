package com.dungeonexplorer.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class GameSessionDTO {
    private UUID sessionId;
    private PlayerDTO player;
    private DungeonDTO dungeon;
    private boolean active;
}
