package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enemy {
    private UUID id;

    private String templateId;

    private String name;

    private Stats stats;

    private int xp;

    private List<Loot> loot;

    private boolean defeated;

    private boolean isBoss;
}
