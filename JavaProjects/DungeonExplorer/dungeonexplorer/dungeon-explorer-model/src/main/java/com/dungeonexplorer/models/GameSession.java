package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSession {
    private UUID sessionId;
    private Player player;
    private Dungeon dungeon;
    private boolean active;

    public GameSession(Player player, Dungeon dungeon){
        sessionId = UUID.randomUUID();
        this.player = player;
        this.dungeon = dungeon;
        active = true;
    }
}
