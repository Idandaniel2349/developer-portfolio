package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private String id;

    private String roomName;

    private String description;

    // from direction to room id
    private Map<String, String> exits = new HashMap<>();

    private List<Loot> loot = new ArrayList<>();;

    private List<Enemy> enemies = new ArrayList<>();

    private boolean isStartingRoom = false;

    private boolean isExitRoom = false;

    private boolean visited = false;

    private boolean isBossRoom = false;

    // coordinates
    private int x = 0;
    private int y = 0;

    public Room(String id, String name, String description){
        this.id = id;
        this.roomName = name;
        this.description = description;
    }
}
