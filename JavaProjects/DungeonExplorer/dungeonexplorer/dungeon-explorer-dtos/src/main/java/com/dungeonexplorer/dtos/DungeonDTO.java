package com.dungeonexplorer.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DungeonDTO {
    private List<FloorDTO> floors;
    private Map<String, RoomDTO> roomById;
}
