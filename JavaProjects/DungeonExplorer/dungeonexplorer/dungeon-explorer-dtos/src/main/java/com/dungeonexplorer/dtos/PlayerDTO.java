package com.dungeonexplorer.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerDTO {
    private UUID id;
    private String name;
    private StatsDTO stats;
    private int xp;
    private int level;
    private int currentFloorNumber;
    private String currentRoomId;
    private InventoryDTO inventory;
}
