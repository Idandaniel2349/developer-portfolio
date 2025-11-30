package com.dungeonexplorer.dtos.responses;

import com.dungeonexplorer.dtos.PlayerDTO;
import lombok.Data;

@Data
public class UseItemResponseDTO {
    private PlayerDTO player;
}
