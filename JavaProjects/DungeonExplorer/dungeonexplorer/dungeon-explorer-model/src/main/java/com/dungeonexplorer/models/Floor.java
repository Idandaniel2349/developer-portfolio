package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Floor {
    private int floorNumber;

    private int numberOfRooms;

    private String startingRoomId;

    private String exitRoomId;

    private List<String> roomIds;

    private boolean isBossDefeated;
}
