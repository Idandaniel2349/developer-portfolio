package com.dungeonexplorer.dtos;

import lombok.Data;

import java.util.List;

@Data
public class FloorDTO {
    private int floorNumber;
    private int numberOfRooms;
    private List<String> roomIds;
    private String startingRoomId;
    private String exitRoomId;
    private boolean isBossDefeated;
}
