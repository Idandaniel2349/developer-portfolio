package com.dungeonexplorer.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class EnemyDTO {
    private UUID id;
    private String templateId;
    private String name;
    private StatsDTO stats;
    private boolean isBoss;
}
