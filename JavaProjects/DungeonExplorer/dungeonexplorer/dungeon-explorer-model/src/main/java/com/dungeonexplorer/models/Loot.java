package com.dungeonexplorer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loot {
    private UUID id;

    private Item item;

    private double dropChance;

    private int quantity;
}
