package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dungeon {
    private List<Floor> floors;

    // map from roomId to room
    private Map<String, Room> roomById = new HashMap<>();
}
