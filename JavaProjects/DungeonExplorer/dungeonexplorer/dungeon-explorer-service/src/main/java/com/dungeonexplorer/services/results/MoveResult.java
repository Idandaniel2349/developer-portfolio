package com.dungeonexplorer.services.results;

import com.dungeonexplorer.models.Loot;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MoveResult {
    private MoveEnum moveEnum;
    private List<Loot> lootReceived = new ArrayList<>();
}
