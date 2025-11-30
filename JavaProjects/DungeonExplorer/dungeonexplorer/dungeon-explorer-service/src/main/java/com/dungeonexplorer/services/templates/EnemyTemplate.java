package com.dungeonexplorer.services.templates;

import com.dungeonexplorer.models.Loot;
import com.dungeonexplorer.models.Stats;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnemyTemplate {
    private String id;

    private String name;

    private int baseHealth;

    private int baseAttack;

    private int baseXp;

    private boolean defeated;

    @JsonProperty("isBoss")
    private boolean isBoss;
}
