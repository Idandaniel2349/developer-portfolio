package com.dungeonexplorer.services.results;

import com.dungeonexplorer.models.Loot;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FightResult {
    private FightEnum fightEnum;
    private List<Loot> lootReceived = new ArrayList<>();
}
