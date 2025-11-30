package com.dungeonexplorer.dtos.responses;

import com.dungeonexplorer.dtos.DungeonDTO;
import com.dungeonexplorer.dtos.LootDTO;
import com.dungeonexplorer.dtos.PlayerDTO;
import com.dungeonexplorer.dtos.RoomDTO;
import com.dungeonexplorer.services.results.MoveEnum;
import com.dungeonexplorer.services.results.MoveResult;
import lombok.Data;

import java.util.List;

@Data
public class MoveResponseDTO {
    private MoveResult moveResult;
    private PlayerDTO player;
    private RoomDTO currentRoom;
}
